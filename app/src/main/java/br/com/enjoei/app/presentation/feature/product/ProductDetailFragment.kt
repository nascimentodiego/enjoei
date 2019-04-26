package br.com.enjoei.app.presentation.feature.product


import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.enjoei.app.R
import br.com.enjoei.app.presentation.extensions.hideStatusBar
import br.com.enjoei.app.presentation.extensions.loadProductPhoto
import br.com.enjoei.app.presentation.extensions.setupToolbar
import br.com.enjoei.app.presentation.extensions.strikethroughSpan
import br.com.enjoei.app.presentation.feature.product.ProductDetailFragmentArgs.Companion.fromBundle
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.item_product.textViewPrice
import kotlinx.android.synthetic.main.item_product.textViewTitle


class ProductDetailFragment : Fragment() {

    val product by lazy {
        fromBundle(arguments!!).product
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar("", toolbar) {
            hideStatusBar()
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        textViewPrice.text = product.price
        val spannableOldPrice = SpannableString(product.oldPrice)
        spannableOldPrice.strikethroughSpan()
        textViewOldPrice.text = spannableOldPrice
        textViewTitle.text = product.title
        textViewDiscountAndInstallment.text = product.installmentAndDiscount
        textViewContent.text = product.content
        textViewCommentCount.text = product.commentCount

        imageViewProductPhoto.loadProductPhoto(product.photos[0])
    }


}
