package com.tmmt.innersect.mvp.view;

import com.tmmt.innersect.mvp.model.CommodityViewModel;
import com.tmmt.innersect.mvp.model.ImageDetail;
import com.tmmt.innersect.mvp.model.SpuViewModel;

/**
 * Created by flame on 2017/5/3.
 */

public interface CommodityView {

    void changeColor(ImageDetail images);
    void fillView(SpuViewModel.Data viewModel);
    void fillView(CommodityViewModel.Commodity viewModel);
    void addSuccess(int count);
    void addFailed();
}
