package net.nashlegend.sourcewall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import net.nashlegend.sourcewall.R;
import net.nashlegend.sourcewall.activities.LoginActivity;
import net.nashlegend.sourcewall.activities.SettingActivity;
import net.nashlegend.sourcewall.request.api.UserAPI;
import net.nashlegend.sourcewall.util.Consts;
import net.nashlegend.sourcewall.util.ImageUtils;
import net.nashlegend.sourcewall.util.Mob;
import net.nashlegend.sourcewall.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {


    @BindView(R.id.image_avatar)
    ImageView imageAvatar;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.profile_header)
    LinearLayout profileHeader;
    @BindView(R.id.layout_msg_center)
    LinearLayout layoutMsgCenter;
    @BindView(R.id.layout_my_favors)
    LinearLayout layoutMyFavors;
    @BindView(R.id.layout_my_posts)
    LinearLayout layoutMyPosts;
    @BindView(R.id.layout_my_replies)
    LinearLayout layoutMyReplies;
    @BindView(R.id.layout_my_questions)
    LinearLayout layoutMyQuestions;
    @BindView(R.id.view_switch_to_day)
    LinearLayout viewSwitchToDay;
    @BindView(R.id.view_switch_to_night)
    LinearLayout viewSwitchToNight;
    @BindView(R.id.layout_setting)
    LinearLayout layoutSetting;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        if (UserAPI.isLoggedIn()) {
            ImageLoader.getInstance().displayImage(UserAPI.getUserAvatar(), imageAvatar, ImageUtils.bigAvatarOptions);
            userName.setText(UserAPI.getName());
        } else {
            imageAvatar.setImageResource(R.drawable.ic_default_avatar_96dp);
            userName.setText(R.string.click_to_login);
        }
        if (SharedPreferencesUtil.readBoolean(Consts.Key_Is_Night_Mode, false)) {
            viewSwitchToDay.setVisibility(View.VISIBLE);
            viewSwitchToNight.setVisibility(View.GONE);
        } else {
            viewSwitchToDay.setVisibility(View.GONE);
            viewSwitchToNight.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.layout_msg_center, R.id.layout_my_favors, R.id.layout_my_posts,
            R.id.layout_my_replies, R.id.layout_my_questions, R.id.view_switch_to_day,
            R.id.view_switch_to_night, R.id.layout_setting, R.id.profile_header})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_header:
                if (!UserAPI.isLoggedIn()) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, Consts.Code_Login);
                }
                break;
            case R.id.layout_msg_center:
                break;
            case R.id.layout_my_favors:
                break;
            case R.id.layout_my_posts:
                break;
            case R.id.layout_my_replies:
                break;
            case R.id.layout_my_questions:
                break;
            case R.id.view_switch_to_day:
            case R.id.view_switch_to_night:
                revertMode();
                break;
            case R.id.layout_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }


    private void revertMode() {
        SharedPreferencesUtil.saveBoolean(Consts.Key_Is_Night_Mode, !SharedPreferencesUtil.readBoolean(Consts.Key_Is_Night_Mode, false));
        MobclickAgent.onEvent(getActivity(), Mob.Event_Switch_Day_Night_Mode);
        getActivity().recreate();
    }

}
