package com.macinternetservices.sablebusinessdirectory.ui.city.cityList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.macinternetservices.sablebusinessdirectory.Config;
import com.macinternetservices.sablebusinessdirectory.MainActivity;
import com.macinternetservices.sablebusinessdirectory.R;
import com.macinternetservices.sablebusinessdirectory.binding.FragmentDataBindingComponent;
import com.macinternetservices.sablebusinessdirectory.databinding.FragmentCityListBinding;
import com.macinternetservices.sablebusinessdirectory.ui.city.adapter.CityAdapter;
import com.macinternetservices.sablebusinessdirectory.ui.common.DataBoundListAdapter;
import com.macinternetservices.sablebusinessdirectory.ui.common.PSFragment;
import com.macinternetservices.sablebusinessdirectory.utils.AutoClearedValue;
import com.macinternetservices.sablebusinessdirectory.utils.Constants;
import com.macinternetservices.sablebusinessdirectory.utils.Utils;
import com.macinternetservices.sablebusinessdirectory.viewmodel.city.CityViewModel;
import com.macinternetservices.sablebusinessdirectory.viewobject.City;
import com.macinternetservices.sablebusinessdirectory.viewobject.common.Status;
import com.macinternetservices.sablebusinessdirectory.viewobject.holder.CityParameterHolder;

import java.util.List;

public class CityListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private CityViewModel cityViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentCityListBinding> binding;
    private AutoClearedValue<CityAdapter> cityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentCityListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if(getActivity() instanceof MainActivity)  {
            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity)getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity)getActivity()).updateMenuIconWhite();
        }

        binding.get().cityListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == cityAdapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !cityViewModel.forceEndLoading) {

                            cityViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.ITEM_COUNT;

                            cityViewModel.offset = cityViewModel.offset + limit;

                            cityViewModel.setNextPageCityListObj(String.valueOf(Config.HOME_PRODUCT_COUNT), String.valueOf(cityViewModel.offset), cityViewModel.cityParameterHolder);

                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            cityViewModel.forceEndLoading = false;

            cityViewModel.loadingDirection = Utils.LoadingDirection.top;

            cityViewModel.setCityListObj(String.valueOf(Config.HOME_PRODUCT_COUNT), Constants.ZERO, cityViewModel.cityParameterHolder);

        });

    }

    @Override
    protected void initViewModels() {

        cityViewModel = new ViewModelProvider(this, viewModelFactory).get(CityViewModel.class);

    }

    @Override
    protected void initAdapters() {

        CityAdapter nvAdapter = new CityAdapter(dataBindingComponent, city -> navigationController.navigateToSelectedCityDetail(getActivity(), city.id, city.name), this);

        this.cityAdapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().cityListRecyclerView.setAdapter(this.cityAdapter.get());

    }

    @Override
    protected void initData() {

        getIntentData();

        cityViewModel.setCityListObj(String.valueOf(Config.HOME_PRODUCT_COUNT), Constants.ZERO, cityViewModel.cityParameterHolder);

        cityViewModel.getCityListData().observe(this, listResource -> {

            if(listResource != null)
            {
                switch (listResource.status){

                    case SUCCESS:

                        if(listResource.data != null)
                        {
                            if(listResource.data.size() > 0)
                            {
                                replaceCity(listResource.data);
                            }
                        }

                        cityViewModel.setLoadingState(false);

                        break;

                    case LOADING:

                        if(listResource.data != null)
                        {
                            if(listResource.data.size() > 0)
                            {
                                replaceCity(listResource.data);
                            }
                        }

                        break;

                    case ERROR:

                        cityViewModel.setLoadingState(false);

                        break;
                }
            }

        });

        cityViewModel.getNextPageCityListData().observe(this, state -> {

            if (state != null) {
                if (state.status == Status.ERROR) {

                    cityViewModel.setLoadingState(false);
                    cityViewModel.forceEndLoading = true;

                }
            }
        });

        cityViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(cityViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void getIntentData()
    {
        if (getActivity() != null)
        {
            cityViewModel.cityParameterHolder = (CityParameterHolder) getActivity().getIntent().getSerializableExtra(Constants.CITY_HOLDER);
        }
        if (getArguments() != null){
            cityViewModel.cityParameterHolder = (CityParameterHolder) getArguments().getSerializable(Constants.CITY_HOLDER);
        }
    }

    private void replaceCity(List<City> cities)
    {
        this.cityAdapter.get().replace(cities);
        binding.get().executePendingBindings();
    }

    @Override
    public void onDispatched() {

    }

}
