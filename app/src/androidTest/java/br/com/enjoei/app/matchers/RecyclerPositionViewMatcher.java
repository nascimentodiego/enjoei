package br.com.enjoei.app.matchers;

import android.content.res.Resources;
import android.view.View;
import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class RecyclerPositionViewMatcher {
    private final int recyclerViewId;

    private RecyclerPositionViewMatcher(int recyclerViewId) {
        this.recyclerViewId = recyclerViewId;
    }

    private Matcher<View> atPosition(final int position) {
        return atPositionOnView(position, -1);
    }

    private Matcher<View> atPositionOnView(final int position, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            public void describeTo(Description description) {
                String idDescription = Integer.toString(recyclerViewId);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(recyclerViewId);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format("%s (resource printerName not found)", recyclerViewId);
                    }
                }

                description.appendText("with id: " + idDescription);
            }

            public boolean matchesSafely(View view) {

                this.resources = view.getResources();

                if (childView == null) {
                    RecyclerView recyclerView = view.getRootView().findViewById(recyclerViewId);
                    if (recyclerView != null && recyclerView.getId() == recyclerViewId) {
                        RecyclerView.ViewHolder childViewHolder
                                = recyclerView.findViewHolderForAdapterPosition(position);
                        if (childViewHolder != null)
                            childView = childViewHolder.itemView;
                    } else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }

            }
        };
    }

    public static Matcher<View> withRecyclerView(@IdRes int recyclerViewId, int atPosition) {
        return new RecyclerPositionViewMatcher(recyclerViewId).atPosition(atPosition);
    }

    public static Matcher<View> withRecyclerViewAndViewId(@IdRes int recyclerViewId, int atPosition, int viewId) {
        return new RecyclerPositionViewMatcher(recyclerViewId).atPositionOnView(atPosition, viewId);
    }
}
