package br.com.enjoei.app.presentation.feature.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.enjoei.app.MainNavDirections
import br.com.enjoei.app.R
import br.com.enjoei.app.presentation.extensions.makeGone
import br.com.enjoei.app.presentation.extensions.makeVisible
import br.com.enjoei.app.presentation.extensions.observeNonNull
import br.com.enjoei.app.presentation.feature.home.adapter.ProductAdapter
import br.com.enjoei.app.presentation.model.ProductItemView
import br.com.enjoei.app.presentation.widget.SpacingItemDecoration
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.content_error.*
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val VISIBLE_THRESHOLD = 1

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private val adapterProduct = ProductAdapter()
    private var isLoadingMore: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadScreen(savedInstanceState)
    }

    private fun loadScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            viewModel.execute(
                HomeViewModel.HomeIntention.LoadScreen
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        bindIntention()
        setupRecycleView()
    }


    private fun setupRecycleView() {
        val spacingItemDecoration =
            SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.smallest))
        val numbOfColumns: Int = resources.getInteger(R.integer.number_of_columns)
        val manager = GridLayoutManager(context, numbOfColumns)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapterProduct.isFirstPosition(position)) manager.spanCount else 1
            }
        }

        with(recyclerView) {
            setHasFixedSize(true)
            adapter = adapterProduct
            layoutManager = manager
            addItemDecoration(spacingItemDecoration)
        }

        swipeRefresh.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorAccent)
        )
        swipeRefresh.setOnRefreshListener {
            viewModel.execute(
                HomeViewModel.HomeIntention.LoadScreen
            )
            swipeRefresh.isRefreshing = true
        }
    }

    private fun bindIntention() {
        val tryAgainClick =
            RxView
                .clicks(buttonTryAgain)
                .map { HomeViewModel.HomeIntention.LoadScreen }

        val clickItem = adapterProduct.clickItem()
            .map {
                HomeViewModel.HomeIntention.LoadProductDetail(it)
            }

        val loadMore = RxRecyclerView
            .scrollEvents(recyclerView)
            .filter {
                val manager = recyclerView.layoutManager as LinearLayoutManager

                val totalItemCount = manager.itemCount
                val lastVisibleItemPosition = manager.findLastVisibleItemPosition()

                !isLoadingMore && totalItemCount <= (lastVisibleItemPosition + VISIBLE_THRESHOLD)
            }
            .map {
                HomeViewModel.HomeIntention.LoadMore
            }

        val observable = Observable.mergeArray(tryAgainClick, loadMore, clickItem)

        viewModel.bindIntent(observable)
    }

    private fun initViewModel() {
        viewModel.sideEffect.observeNonNull(this) {
            it.getContentIfNotHandled()?.let { sideEffect ->
                when (sideEffect) {
                    is HomeViewModel.HomeSideEffect.NavigateToDetail -> navigateToDetailScreen(sideEffect.productView)
                }
            }
        }

        viewModel.states.observeNonNull(this) { state ->

            isLoadingMore = state.isLoading || state.isLoadingMore
            with(state) {
                when {
                    isLoadError -> showError()
                    isLoading -> showLoading(true)
                    isLoadingMore -> showLoading(false)
                    else -> showData(state)
                }
            }
        }
    }

    private fun showData(state: HomeViewModel.HomeScreenState) {
        adapterProduct.items = state.productList
        recyclerView.makeVisible()
        loadingGroup.makeGone()
        errorGroup.makeGone()
        swipeRefresh.isRefreshing = false
    }

    private fun showLoading(shouldShowLoading: Boolean) {
        if (shouldShowLoading) {
            loadingGroup.makeVisible()
            recyclerView.makeGone()
            errorGroup.makeGone()
        }
    }


    private fun showError() {
        loadingGroup.makeGone()
        recyclerView.makeGone()
        errorGroup.makeVisible()
        swipeRefresh.isRefreshing = false
    }

    private fun navigateToDetailScreen(product: ProductItemView) {
        val direction =  MainNavDirections.actionMainFragmentToProductDetail(product)
        findNavController().navigate(direction)
    }

}
