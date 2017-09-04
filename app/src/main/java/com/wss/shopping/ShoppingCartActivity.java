package com.wss.shopping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wss.shopping.adapter.CartListAdapter;
import com.wss.shopping.dao.DBHelper;
import com.wss.shopping.entity.ShoppingCartBean;
import com.wss.shopping.listener.OnShoppingCartChangeListener;
import com.wss.shopping.listener.ResponseCallBack;
import com.wss.shopping.listener.ShoppingCartBiz;
import com.wss.shopping.listener.ShoppingCartHttpBiz;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Describe：购物车
 * Created by 吴天强 on 2017/9/1.
 */

public class ShoppingCartActivity extends Activity {

    @BindView(R.id.cart_list)
    ExpandableListView expandableListView;

    @BindView(R.id.iv_check_all)
    ImageView ivSelectAll;

    @BindView(R.id.btnSettle)
    TextView btnSettle;

    @BindView(R.id.tv_count_money)
    TextView tvCountMoney;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.cart_empty)
    RelativeLayout rlShoppingCartEmpty;

    @BindView(R.id.bottom_bar)
    RelativeLayout rlBottomBar;

    @BindView(R.id.tv_edit_all)
    TextView tvEdit;//编辑操作


    private List<ShoppingCartBean> mListGoods = new ArrayList<>();
    private CartListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        DBHelper.init(getApplicationContext());
        setAdapter();
        requestShoppingCartList();

    }


    private void setAdapter() {
        adapter = new CartListAdapter(this);
        expandableListView.setAdapter(adapter);
        adapter.setOnShoppingCartChangeListener(new OnShoppingCartChangeListener() {

            public void onDataChange(String selectCount, String selectMoney) {
                int goodsCount = ShoppingCartBiz.getGoodsCount();
                if (goodsCount == 0) {
                    showEmpty(true);
                } else {
                    showEmpty(false);//其实不需要做这个判断，因为没有商品的时候，必须退出去添加商品；
                }
                String countMoney = String.format(getResources().getString(R.string.count_money), selectMoney);
                String countGoods = String.format(getResources().getString(R.string.count_goods), selectCount);
                String title = String.format(getResources().getString(R.string.shop_title), goodsCount + "");
                tvCountMoney.setText(countMoney);
                btnSettle.setText(countGoods);
                tvTitle.setText(title);
            }

            public void onSelectItem(boolean isSelectedAll) {
                ShoppingCartBiz.checkItem(isSelectedAll, ivSelectAll);
            }
        });
        //通过监听器关联Activity和Adapter的关系，解耦；
        View.OnClickListener listener = adapter.getAdapterListener();
        if (listener != null) {
            //即使换了一个新的Adapter，也要将“全选事件”传递给adapter处理；
            ivSelectAll.setOnClickListener(adapter.getAdapterListener());
            //结算时，一般是需要将数据传给订单界面的
            btnSettle.setOnClickListener(adapter.getAdapterListener());
            tvEdit.setOnClickListener(adapter.getAdapterListener());

        }
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }

    public void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            expandableListView.setVisibility(View.GONE);
            rlShoppingCartEmpty.setVisibility(View.VISIBLE);
            rlBottomBar.setVisibility(View.GONE);
        } else {
            expandableListView.setVisibility(View.VISIBLE);
            rlShoppingCartEmpty.setVisibility(View.GONE);
            rlBottomBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取购物车列表的数据（数据和网络请求也是非通用部分）
     */
    private void requestShoppingCartList() {
        ShoppingCartBiz.delAllGoods();
        testAddGood();
        //使用本地JSON，作测试用。本来应该是将商品ID发送的服务器，服务器返回对应的商品信息；
        ShoppingCartHttpBiz.requestOrderList(this, new ResponseCallBack() {//requestOrderList(list, new VollyHelperNew.ResponseCallBack())


            public void handleResponse(Object o, int code) {
                // TODO Auto-generated method stub
                mListGoods = ShoppingCartHttpBiz.handleOrderList((JSONObject) o, code);
                tvEdit.setVisibility(mListGoods.size() > 0 ? View.VISIBLE : View.GONE);
                ShoppingCartBiz.updateShopList(mListGoods);
                updateListView();
            }
        });
    }

    private void updateListView() {
        adapter.setList(mListGoods);
        adapter.notifyDataSetChanged();
        expandAllGroup();
    }

    /**
     * 展开所有组
     */
    private void expandAllGroup() {
        for (int i = 0; i < mListGoods.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    /**
     * 测试添加数据 ，添加的动作是通用的，但数据上只是添加ID而已，数据非通用
     */
    private void testAddGood() {
        ShoppingCartBiz.addGoodToCart("279457f3-4692-43bf-9676-fa9ab9155c38", "6");
        ShoppingCartBiz.addGoodToCart("95fbe11d-7303-4b9f-8ca4-537d06ce2f8a", "8");
        ShoppingCartBiz.addGoodToCart("8c6e52fb-d57c-45ee-8f05-50905138801b", "9");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801d", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801e", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801f", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801g", "3");
        ShoppingCartBiz.addGoodToCart("7d6e52fb-d57c-45ee-8f05-50905138801h", "3");
    }

}
