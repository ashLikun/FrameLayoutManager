package com.azoft.carousellayoutmanager.sample;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.DefaultZoomOnLayoutListener;
import com.azoft.carousellayoutmanager.FrameLayoutManager;
import com.azoft.carousellayoutmanager.FrameScrollListener;
import com.azoft.carousellayoutmanager.sample.databinding.ActivityCarouselPreviewBinding;
import com.azoft.carousellayoutmanager.sample.databinding.ItemViewBinding;

import java.util.Random;

public class CarouselPreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityCarouselPreviewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_carousel_preview);

        setSupportActionBar(binding.toolbar);

        final TestAdapter adapter = new TestAdapter();
        final TestAdapter adapter2 = new TestAdapter();

        // create layout manager with needed params: vertical, cycle
        initRecyclerView(binding.listHorizontal, new FrameLayoutManager(FrameLayoutManager.HORIZONTAL, false), adapter);
        initRecyclerView(binding.listVertical, new FrameLayoutManager(FrameLayoutManager.VERTICAL, true), adapter);

        // fab button will add element to the end of the list
        binding.fabScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
/*
                final int itemToRemove = adapter.mItemsCount;
                if (10 != itemToRemove) {
                    adapter.mItemsCount++;
                    adapter.notifyItemInserted(itemToRemove);
                }
*/
                binding.listHorizontal.smoothScrollToPosition(adapter.getItemCount() - 2);
                binding.listVertical.smoothScrollToPosition(adapter.getItemCount() - 2);
            }
        });

        // fab button will remove element from the end of the list
        binding.fabChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
/*
                final int itemToRemove = adapter.mItemsCount - 1;
                if (0 <= itemToRemove) {
                    adapter.mItemsCount--;
                    adapter.notifyItemRemoved(itemToRemove);
                }
*/
                binding.listHorizontal.smoothScrollToPosition(1);
                binding.listVertical.smoothScrollToPosition(1);
            }
        });
    }

    private void initRecyclerView(final RecyclerView recyclerView, final FrameLayoutManager layoutManager, final TestAdapter adapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setOnLayoutListener(new DefaultZoomOnLayoutListener());
        layoutManager.setMaxVisibleItems(3);

        recyclerView.setLayoutManager(layoutManager);
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true);
        // sample adapter with random data
        recyclerView.setAdapter(adapter);
        // enable center post scrolling
        recyclerView.addOnScrollListener(new FrameScrollListener());



        layoutManager.addOnItemSelectionListener(new FrameLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                if (FrameLayoutManager.INVALID_POSITION != adapterPosition) {
                    final int value = adapter.mPosition[adapterPosition];
                }
            }
        });
    }

    private final class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {

        @SuppressWarnings("UnsecureRandomNumberGeneration")
        private final Random mRandom = new Random();
        private final int[] mColors;
        private final int[] mPosition;
        private int mItemsCount = 10;

        TestAdapter() {
            mColors = new int[mItemsCount];
            mPosition = new int[mItemsCount];
            for (int i = 0; mItemsCount > i; ++i) {
                //noinspection MagicNumber
                mColors[i] = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256));
                mPosition[i] = i;
            }
        }

        @Override
        public TestViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            return new TestViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }

        @Override
        public void onBindViewHolder(final TestViewHolder holder, final int position) {
            holder.mItemViewBinding.cItem1.setText(String.valueOf(mPosition[position]));
            holder.mItemViewBinding.cItem2.setText(String.valueOf(mPosition[position]));
            holder.itemView.setBackgroundColor(mColors[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(FrameLayoutManager.handleItemClick(v, position)) {
//                        Toast.makeText(v.getContext(), "aaaaa" + position, Toast.LENGTH_SHORT).show();
//                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return mItemsCount;
        }
    }

    private static class TestViewHolder extends RecyclerView.ViewHolder {

        private final ItemViewBinding mItemViewBinding;

        TestViewHolder(final ItemViewBinding itemViewBinding) {
            super(itemViewBinding.getRoot());

            mItemViewBinding = itemViewBinding;
        }
    }
}