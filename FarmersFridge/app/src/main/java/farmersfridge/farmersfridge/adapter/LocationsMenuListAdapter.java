package farmersfridge.farmersfridge.adapter;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.menu.MenuDetail;
import farmersfridge.farmersfridge.models.CathegoryModel;
import farmersfridge.farmersfridge.models.MenuItemModel;
import farmersfridge.farmersfridge.models.MenuModel;
import farmersfridge.farmersfridge.support.CustomVolleyRequest;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 8/11/2016.
 */
public class LocationsMenuListAdapter extends BaseAdapter {
    private static List<CathegoryModel> categoryList;
    private static List<List<MenuItemModel>> menuItemList;
    private ImageLoader imageLoader;
    private Context context;
    private TextView title;
    private TextView header;
    private NetworkImageView imageview;
    private ImageView favorite;
    private static Boolean isFavourite;
    private RelativeLayout layForMenuImg;
    public static String favName;
    private LinearLayout layForMenus;
    private ImageView imgForRight, imgForLeft;
    private ListView listView;
    private ArrayList<HashMap<String, Integer>> arrayValue = new ArrayList<>();
    private boolean keyPresent = false;

    public LocationsMenuListAdapter(Context context, List<CathegoryModel> categoryList, List<List<MenuItemModel>> menuItemList, ListView listView) {
        this.categoryList = categoryList;
        this.menuItemList = menuItemList;
        this.listView = listView;
        this.context = context;
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.menu_horiztl, null);
        CathegoryModel categoryModel = categoryList.get(i);
        final HorizontalScrollView scrollView = (HorizontalScrollView)v.findViewById(R.id.hrztlView);
        imgForRight = (ImageView) v.findViewById(R.id.imgForArrowOff);
        imgForLeft = (ImageView) v.findViewById(R.id.imgForArrowOffLeft);
        scrollView.setId(i);
        imgForRight.setId(i);
        imgForLeft.setId(i);
        List<MenuItemModel> menuList = menuItemList.get(i);
        final LinearLayout topLinearLayout = new LinearLayout(context);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int j = 0; j < menuList.size(); j++){
            View viewImage = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.menu_items, null);

            MenuItemModel menuItemModel = menuList.get(j);

            imageview = (NetworkImageView)viewImage.findViewById(R.id.imgForDish);
            title =  (TextView) viewImage.findViewById(R.id.txtForItemName);
            header = (TextView) viewImage.findViewById(R.id.txtForHeader);
            favorite = (ImageView)viewImage.findViewById(R.id.imgForFav);
            layForMenus = (LinearLayout) viewImage.findViewById(R.id.layForMenus);
            layForMenuImg = (RelativeLayout) viewImage.findViewById(R.id.layForMenuImg);

            Typeface headerFnt = Typeface.createFromAsset(context.getAssets(), "fonts/GrottoIronic-SemiBoldExpanded.otf");
            Typeface titleFnt = Typeface.createFromAsset(context.getAssets(), "fonts/GarageFonts - FreightMicro Pro Medium Italic.otf");
            header.setTypeface(headerFnt);
            title.setTypeface(titleFnt);

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;

            layForMenus.setLayoutParams(new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT));

            if(i%2 != 0) {
                layForMenuImg.setBackgroundResource(R.drawable.menu_brown_bg);
            } else {
                layForMenuImg.setBackgroundResource(R.drawable.menu_green_bg);
            }

            title.setText(menuItemModel.getTitle());
            header.setText(categoryModel.getName());

            imageview.setId(i);
            imageview.setTag(j);
            favorite.setId(i);
            favorite.setTag(j);
            imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            imageLoader.get(menuItemModel.getImageUrl(), ImageLoader.getImageListener(imageview, R.drawable.placeholder, R.drawable.placeholder));
            imageview.setImageUrl(menuItemModel.getImageUrl(), imageLoader);
            topLinearLayout.addView(viewImage);

            isFavourite = menuItemModel.isFavourite();
            if (isFavourite){
                favorite.setImageResource(R.drawable.favorite_selected);
            }else{
                favorite.setImageResource(R.drawable.favorite_unselected);
            }

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    int arrayPosition = v.getId();
                    List<MenuItemModel> menuList = Constants.locMenuArray.get(arrayPosition);
                    MenuItemModel menuItemModel = menuList.get(position);
                    isFavourite = menuItemModel.isFavourite();
                    favName = menuItemModel.getTitle();
                    if (isFavourite) {
                        SessionStores.MENU_MODEL = new MenuModel(menuItemModel.getTitle());
                    //    new DeleteFavLocationTask(context, SessionStores.MENU_MODEL.deleteFavourites(), v, menuList, position, arrayPosition, "list");
                    } else {
                        SessionStores.MENU_MODEL = new MenuModel(menuItemModel.getTitle());
                    //    new UpdateFavLocationTask(context, SessionStores.MENU_MODEL.updateFavourites(), v, menuList, position, arrayPosition, "list");
                    }
                }
            });

            imageview.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    Log.e("Tag",""+imageview.getTag());
                    Context c = v.getContext();
                    int position = (Integer) v.getTag();
                    int arrayPosition = v.getId();
                    Log.e("position",""+position);
                    List<MenuItemModel> menuList = Constants.locMenuArray.get(arrayPosition);
                    MenuItemModel menuItemModel = menuList.get(position);
                    String categoryFetch = menuItemModel.getCategory();
                   /* Log.e("menu item title clicks?",""+menuItemModel.getTitle());*/
                    //            TabHostFragments tabHostFragments = (TabHostFragments)c;
                    FragmentMain fragmentMain = (FragmentMain)c;
                    FragmentManager fragmentManager = fragmentMain.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MenuDetail fragment = new MenuDetail();
                    Bundle bundle = new Bundle();
                    bundle.putInt("menuItem", position);
                    bundle.putInt("categoryPos", arrayPosition);
                    bundle.putString("category", categoryFetch);
                    bundle.putParcelableArrayList("arrayList", (ArrayList<? extends Parcelable>) menuList);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.realtabcontent, fragment);
                    //            fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });


        }

        scrollView.addView(topLinearLayout);

        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);
                    int width = metrics.widthPixels;
                    int center = scrollView.getScrollX() + scrollView.getWidth() / 2;
                    Log.e("center??",""+center);

                    Timer ntimer = new Timer();
                    ntimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int chilrenNum = topLinearLayout.getChildCount();

                            int previousView = 0;
                            for (int i = 0; i < chilrenNum; i++) {
                                View view = topLinearLayout.getChildAt(i);
                                int viewLeft = view.getLeft();
                                int viewWidth = view.getWidth();
                                Log.e("view left??",""+viewLeft);
                                Log.e("view width??",""+viewWidth);

                                int currentScrollPos = scrollView.getScrollX();
                                Log.e("current scrool pos??",""+currentScrollPos);

                                if(viewLeft > currentScrollPos) {
                                    scrollView.scrollTo(previousView, 0);
                                    break;
                                } else {
                                    previousView = viewLeft;
                                }
                            }
                        }
                    },2000);
                }
                return false;
            }
        });

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        final int width = metrics.widthPixels;

        imgForRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxScrollX = scrollView.getChildAt(0).getMeasuredWidth()-scrollView.getMeasuredWidth();
                if (scrollView.getScrollX() == maxScrollX) {
                    Log.e("reached","end");
                } else {
                    int widthXValue = 0;
                    int id = view.getId();
                    if(arrayValue.size() > 0) {
                        for (int i = 0; i < arrayValue.size(); i++) {
                            if (arrayValue.get(i).containsKey("" + i)) {
                                String keyValue = arrayValue.get(i).keySet().toString();
                                String keySplit = keyValue.replace("[", "");
                                String keySplit2 = keySplit.replace("]", "");
                                if(keySplit2.equals(String.valueOf(id))) {
                                    keyPresent = true;
                                    widthXValue = arrayValue.get(i).get("" + i);
                                }
                            }

                            if (i == arrayValue.size() - 1) {
                                if (keyPresent == false) {
                                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                                    hashMap.put("" + id, width);
                                    arrayValue.add(hashMap);
                                }
                            }
                        }
                    } else {
                        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                        hashMap.put("" + id, width);
                        arrayValue.add(hashMap);
                    }
                    int x = widthXValue + width;
                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                    hashMap.put("" + id, x);
                    arrayValue.add(id, hashMap);
                    ObjectAnimator animator=ObjectAnimator.ofInt(scrollView, "scrollX", x);
                    animator.setDuration(800);
                    animator.start();
                }
            }
        });

        imgForLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxScrollX = scrollView.getChildAt(0).getMeasuredWidth()-scrollView.getMeasuredWidth();
                if (scrollView.getScrollX() == 0) {
                    Log.e("reached","first");
                } else {
                    int widthXValue = 0;
                    int id = view.getId();
                    if(arrayValue.size() > 0) {
                        for (int i = 0; i < arrayValue.size(); i++) {
                            if (arrayValue.get(i).containsKey("" + i)) {
                                String keyValue = arrayValue.get(i).keySet().toString();
                                String keySplit = keyValue.replace("[", "");
                                String keySplit2 = keySplit.replace("]", "");
                                if(keySplit2.equals(String.valueOf(id))) {
                                    keyPresent = true;
                                    widthXValue = arrayValue.get(i).get("" + i);
                                }
                            }

                            if (i == arrayValue.size() - 1) {
                                if (keyPresent == false) {
                                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                                    hashMap.put("" + id, width);
                                    arrayValue.add(hashMap);
                                }
                            }
                        }
                    } else {
                        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                        hashMap.put("" + id, width);
                        arrayValue.add(hashMap);
                    }
                    int x = widthXValue - width;
                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
                    hashMap.put("" + id, x);
                    arrayValue.add(id, hashMap);

                    ObjectAnimator animator=ObjectAnimator.ofInt(scrollView, "scrollX", x);
                    animator.setDuration(800);
                    animator.start();
                }
            }
        });
        return v;
    }

    public static void favImgUpdated(View v) {
        if (isFavourite) {
            isFavourite = false;
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_unselected);
        } else {
            isFavourite = true;
            ImageView imageView = (ImageView) v;
            imageView.setImageResource(R.drawable.favorite_selected);
        }
    }
}
