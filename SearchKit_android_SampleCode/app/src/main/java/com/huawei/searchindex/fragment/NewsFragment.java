/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.searchindex.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.searchkit.bean.NewsItem;
import com.huawei.searchindex.R;
import com.huawei.searchindex.activity.NewsActivity;
import com.huawei.searchindex.adapter.ContentAdapter;
import com.huawei.searchindex.bean.ListBean;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private static final String TAG = WebFragment.class.getSimpleName();
    private View view;
    private RecyclerView recyclerView;
    private ContentAdapter adapter;
    private TextView tvEmpty;

    /**
     * New instance main fragment.
     *
     * @return the main fragment
     */
    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);

        initView();

        return view;
    }

    private void initView() {
        if (view != null) {
            recyclerView = view.findViewById(R.id.recycler_view);
            tvEmpty = view.findViewById(R.id.tv_empty);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        } else {
            Log.e(TAG, "view is null");
        }
    }

    public void setValue(List<NewsItem> list) {
        List<ListBean> listBeans = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                ListBean bean = new ListBean();
                if (list.get(i).getTitle() != null) {
                    bean.setTitle(list.get(i).getTitle());
                }
                if (list.get(i).getThumbnail() != null && list.get(i).getThumbnail().getUrl() != null) {
                    bean.setUrl(list.get(i).getThumbnail().getUrl());
                } else {
                    bean.setUrl("");
                }
                if (list.get(i).getClickUrl() != null) {
                    bean.setClick_url(list.get(i).getClickUrl());
                }
                listBeans.add(bean);
            }
            if (adapter != null) {
                adapter.refresh(listBeans);
                if (recyclerView != null && tvEmpty != null) {
                    tvEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                adapter = new ContentAdapter(getActivity(), listBeans);
                if (recyclerView != null && tvEmpty != null) {
                    recyclerView.setAdapter(adapter);
                    tvEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            onClick(list);
        } else {
            if (recyclerView != null && tvEmpty != null) {
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void onClick(final List<NewsItem> list) {
        if (adapter != null) {
            adapter.setOnClickListener(
                    new ContentAdapter.OnItemClickListener() {
                        @Override
                        public void click(int position) {
                            Intent intent = new Intent(getActivity(), NewsActivity.class);
                            if (list.get(position).getTitle() != null) {
                                intent.putExtra("news_title", list.get(position).getTitle());
                            } else {
                                intent.putExtra("news_title", "");
                            }
                            if (list.get(position).getClickUrl() != null) {
                                intent.putExtra("news_click_url", list.get(position).getClickUrl());
                            } else {
                                intent.putExtra("news_click_url", "");
                            }
                            if (list.get(position).getPublishTime() != null) {
                                intent.putExtra("news_publish", list.get(position).getPublishTime());
                            } else {
                                intent.putExtra("news_publish", "");
                            }
                            if (list.get(position).getThumbnail() != null
                                    && list.get(position).getThumbnail().getUrl() != null) {
                                intent.putExtra("news_thumbnail", list.get(position).getThumbnail().getUrl());
                            } else {
                                intent.putExtra("news_thumbnail", "");
                            }
                            if (list.get(position).getProvider() != null
                                    && list.get(position).getProvider().getSiteName() != null) {
                                intent.putExtra("news_site_name", list.get(position).getProvider().getSiteName());
                            } else {
                                intent.putExtra("news_site_name", "");
                            }
                            if (list.get(position).getProvider() != null
                                    && list.get(position).getProvider().getLogo() != null) {
                                intent.putExtra("news_logo", list.get(position).getProvider().getLogo());
                            } else {
                                intent.putExtra("news_logo", "");
                            }
                            startActivity(intent);
                        }
                    });
        }
    }
}
