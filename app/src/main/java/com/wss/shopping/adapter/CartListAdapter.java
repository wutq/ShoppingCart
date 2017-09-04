package com.wss.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wss.shopping.R;
import com.wss.shopping.entity.ShoppingCartBean;
import com.wss.shopping.listener.OnShoppingCartChangeListener;
import com.wss.shopping.listener.ShoppingCartBiz;
import com.wss.shopping.view.AlertDialog;
import com.wss.shopping.view.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Describe：购物车列表适配器
 * Created by 吴天强 on 2017/9/1.
 */
public class CartListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShoppingCartBean> mListGoods = new ArrayList<>();
    private OnShoppingCartChangeListener mChangeListener;
    private boolean isSelectAll = false;

    public CartListAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<ShoppingCartBean> mListGoods) {
        this.mListGoods = mListGoods;
        setSettleInfo();
    }

    public void setOnShoppingCartChangeListener(OnShoppingCartChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public View.OnClickListener getAdapterListener() {
        return listener;
    }


    public int getGroupCount() {
        return mListGoods.size();
    }


    public int getChildrenCount(int groupPosition) {
        return mListGoods.get(groupPosition).getGoods().size();
    }


    public Object getGroup(int groupPosition) {
        return mListGoods.get(groupPosition);
    }


    public Object getChild(int groupPosition, int childPosition) {
        return mListGoods.get(groupPosition).getGoods().get(childPosition);
    }


    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public boolean hasStableIds() {
        return false;
    }


    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_group_test, parent, false);
            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvShopNameGroup);
            holder.ivCheckGroup = (ImageView) convertView.findViewById(R.id.ivCheckGroup);
            holder.tvAddMore = (TextView) convertView.findViewById(R.id.tv_add_more);
            holder.tvCoupon = convertView.findViewById(R.id.ll_coupon);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tvGroup.setText(mListGoods.get(groupPosition).getMerchantName());
        ShoppingCartBiz.checkItem(mListGoods.get(groupPosition).isGroupSelected(), holder.ivCheckGroup);
        //写一个控制界面效果的伪操作
        if (mListGoods.size() % (groupPosition + 1) == 0) {
            holder.tvCoupon.setVisibility(View.VISIBLE);
        } else {
            holder.tvCoupon.setVisibility(View.GONE);
        }


        holder.ivCheckGroup.setTag(groupPosition);
        holder.ivCheckGroup.setOnClickListener(listener);
        holder.tvAddMore.setOnClickListener(listener);
        holder.tvCoupon.setOnClickListener(listener);
        holder.tvGroup.setOnClickListener(listener);
        return convertView;
    }

    /**
     * child view
     */

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_child_test, parent, false);
            holder.tvChild = (TextView) convertView.findViewById(R.id.tvItemChild);
            holder.ivCheckGood = (ImageView) convertView.findViewById(R.id.ivCheckGood);
            holder.llGoodInfo = (LinearLayout) convertView.findViewById(R.id.llGoodInfo);
            holder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);
            holder.ivReduce = (ImageView) convertView.findViewById(R.id.ivReduce);
            holder.tvGoodsParam = (TextView) convertView.findViewById(R.id.tvGoodsParam);
            holder.tvNum2 = (TextView) convertView.findViewById(R.id.tvNum2);
            holder.tvPriceNew = (TextView) convertView.findViewById(R.id.tvPriceNew);
//            holder.rlEditStatus = (RelativeLayout) convertView.findViewById(R.id.rlEditStatus);
//            holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
//            holder.tvDel = (TextView) convertView.findViewById(R.id.tvDel);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        ShoppingCartBean.Goods goods = mListGoods.get(groupPosition).getGoods().get(childPosition);
        boolean isChildSelected = mListGoods.get(groupPosition).getGoods().get(childPosition).isChildSelected();
        boolean isEditing = goods.isEditing();
        String priceNew = "¥" + goods.getPrice();
        String num = goods.getNumber();
        String pdtDesc = goods.getPdtDesc();
        String goodName = mListGoods.get(groupPosition).getGoods().get(childPosition).getGoodsName();

        holder.ivCheckGood.setTag(groupPosition + "," + childPosition);
        holder.tvChild.setText(goodName);
        holder.tvPriceNew.setText(priceNew);
        holder.tvNum2.setText(num);
        holder.tvGoodsParam.setText(pdtDesc);

        holder.ivAdd.setTag(goods);
        holder.ivReduce.setTag(goods);

        ShoppingCartBiz.checkItem(isChildSelected, holder.ivCheckGood);
        if (isEditing) {
            holder.llGoodInfo.setVisibility(View.GONE);
        } else {
            holder.llGoodInfo.setVisibility(View.VISIBLE);
        }

        holder.ivCheckGood.setOnClickListener(listener);
        holder.ivAdd.setOnClickListener(listener);
        holder.ivReduce.setOnClickListener(listener);
        holder.llGoodInfo.setOnClickListener(listener);
        return convertView;
    }


    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
                //main
                case R.id.iv_check_all:
                    isSelectAll = ShoppingCartBiz.selectAll(mListGoods, isSelectAll, (ImageView) v);
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                case R.id.tv_edit_all:
                    //编辑所有商品。
                    break;
                case R.id.btnSettle:
                    if (ShoppingCartBiz.hasSelectedGoods(mListGoods)) {
                        Utils.toast("结算跳转");
                    } else {
                        Utils.toast("先选择商品！");
                    }
                    break;
                case R.id.ivCheckGroup:
                    int groupPosition3 = Integer.parseInt(String.valueOf(v.getTag()));
                    isSelectAll = ShoppingCartBiz.selectGroup(mListGoods, groupPosition3);
                    selectAll();
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                //child
                case R.id.ivCheckGood:
                    String tag = String.valueOf(v.getTag());
                    if (tag.contains(",")) {
                        String s[] = tag.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        isSelectAll = ShoppingCartBiz.selectOne(mListGoods, groupPosition, childPosition);
                        selectAll();
                        setSettleInfo();
                        notifyDataSetChanged();
                    }
                    break;

                case R.id.ivAdd:
                    ShoppingCartBiz.addOrReduceGoodsNum(true, (ShoppingCartBean.Goods) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.ivReduce:
                    ShoppingCartBiz.addOrReduceGoodsNum(false, (ShoppingCartBean.Goods) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.llGoodInfo:
                    Utils.toast("商品详情，暂未实现");
                    break;
                case R.id.tvShopNameGroup:
                    Utils.toast("商铺详情，暂未实现");
                    break;
                case R.id.tv_add_more:
                    Utils.toast("凑单");
                    break;
                case R.id.ll_coupon:
                    Utils.toast("优惠券");
                    break;
            }
        }
    };

    private void selectAll() {
        if (mChangeListener != null) {
            mChangeListener.onSelectItem(isSelectAll);
        }
    }

    private void setSettleInfo() {
        String[] infos = ShoppingCartBiz.getShoppingCount(mListGoods);
        //删除或者选择商品之后，需要通知结算按钮，更新自己的数据；
        if (mChangeListener != null && infos != null) {
            mChangeListener.onDataChange(infos[0], infos[1]);
        }
    }

    private void showDelDialog(final int groupPosition, final int childPosition) {
        final AlertDialog delDialog = new AlertDialog(mContext, "温馨提示", "确认删除该商品吗?",
                "取消", "确定");
        delDialog.show();

        delDialog.setClicklistener(new AlertDialog.ClickListenerInterface() {


                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }


                                       public void doRight() {
                                           String productID = mListGoods.get(groupPosition).getGoods().get(childPosition).getProductID();
                                           ShoppingCartBiz.delGood(productID);
                                           delGoods(groupPosition, childPosition);
                                           setSettleInfo();
                                           notifyDataSetChanged();
                                           delDialog.dismiss();
                                       }
                                   }
        );
    }

    private void delGoods(int groupPosition, int childPosition) {
        mListGoods.get(groupPosition).getGoods().remove(childPosition);
        if (mListGoods.get(groupPosition).getGoods().size() == 0) {
            mListGoods.remove(groupPosition);
        }
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        TextView tvGroup;
        //        TextView tvEdit;
        ImageView ivCheckGroup;
        TextView tvAddMore;//凑单
        View tvCoupon;//优惠券
    }

    class ChildViewHolder {
        /**
         * 商品名称
         */
        TextView tvChild;
        /**
         * 商品规格
         */
        TextView tvGoodsParam;
        /**
         * 选中
         */
        ImageView ivCheckGood;
        /**
         * 非编辑状态
         */
        LinearLayout llGoodInfo;
        /**
         * +1
         */
        ImageView ivAdd;
        /**
         * -1
         */
        ImageView ivReduce;

        /**
         * 新价格
         */
        TextView tvPriceNew;
        /**
         * 编辑状态的数量
         */
        TextView tvNum2;
    }
}
