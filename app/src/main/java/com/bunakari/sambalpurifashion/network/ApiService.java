package com.bunakari.sambalpurifashion.network;



import com.bunakari.sambalpurifashion.model.AddressList;
import com.bunakari.sambalpurifashion.model.BalanceResponse;
import com.bunakari.sambalpurifashion.model.BannerList;
import com.bunakari.sambalpurifashion.model.CartList;
import com.bunakari.sambalpurifashion.model.CategoryList;
import com.bunakari.sambalpurifashion.model.ChecksumResponse;
import com.bunakari.sambalpurifashion.model.ContactUsResponse;
import com.bunakari.sambalpurifashion.model.LoginResponse;
import com.bunakari.sambalpurifashion.model.NetworkList;
import com.bunakari.sambalpurifashion.model.OrderDetailsList;
import com.bunakari.sambalpurifashion.model.OrderList;
import com.bunakari.sambalpurifashion.model.OtpResponse;
import com.bunakari.sambalpurifashion.model.ProductList;
import com.bunakari.sambalpurifashion.model.ProfileResponse;
import com.bunakari.sambalpurifashion.model.ReletedVideoList;
import com.bunakari.sambalpurifashion.model.SignupResponse;
import com.bunakari.sambalpurifashion.model.SingleResponse;
import com.bunakari.sambalpurifashion.model.TrailersList;
import com.bunakari.sambalpurifashion.model.TransList;
import com.bunakari.sambalpurifashion.model.VideoList;
import com.bunakari.sambalpurifashion.model.WishlistList;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {
    @POST("api/user_register.php")
    @FormUrlEncoded
    Call<LoginResponse> signUp(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mob") String mobile,
            @Field("address") String address,
            @Field("state") String state,
            @Field("appreferralcode") String referralid,
            @Field("password") String password
    );

    @GET("api/sms.php")
    Call<OtpResponse> getOtp(@Query("mob") String mobile);

    @GET("api/checkuser.php")
    Call<LoginResponse> getCheckUser(@Query("mono") String mobile);

    @GET("api/user_login.php")
    Call<LoginResponse> login(@Query("mobile") String mobile, @Query("password") String password);

    @GET("api/banners.php")
    Call<BannerList> getBanner();

    @GET("api/home-page-listing.php")
    Call<CategoryList> getCategory();

    @GET("api/myprofile.php")
    Call<ProfileResponse> getProfile(@Query("id") String uid);

    @POST("api/upd_profile.php")
    @FormUrlEncoded
    Call<ProfileResponse> submitProfile(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("email") String email,
            @Field("mob") String mobile,
            @Field("dob") String dob,
            @Field("address") String address,
            @Field("state") String state,
            @Field("pincode") String pincode
    );
    @GET("api/products.php")
    Call<ProductList> getProducts(@Query("sid") String sid, @Query("mono") String mono);
    @GET("api/products.php")
    Call<ProductList> gethomeProducts(@Query("sid") String sid, @Query("mono") String mono);
    @GET("api/reletedproduct.php")
    Call<ProductList> getRelProducts(@Query("sid") String sid, @Query("mono") String mono);
    @GET("api/search.php")
    Call<ProductList> getSearch(@Query("mono") String mono, @Query("proname") String pname);

    @GET("api/add-wishlist.php")
    Call<SignupResponse> addWishlist(@Query("pid") String sid, @Query("mono") String mono);

    @GET("api/add-likelist.php")
    Call<SignupResponse> addLikelist(@Query("pid") String pid, @Query("mono") String mono);


    @GET("api/remove-wishlist.php")
    Call<SignupResponse> removeWishlist(@Query("pid") String sid, @Query("mono") String mono);

    @GET("api/remove-likelist.php")
    Call<SignupResponse> removeLikelist(@Query("pid") String sid, @Query("mono") String mono);


    @GET("api/wishlist.php")
    Call<WishlistList> getWishlist(@Query("mono") String mono);
    @GET("api/addtotempnot.php")
    Call<SignupResponse> addtoTempNoti(@Query("pid") String pid, @Query("mono") String mono);

    @POST("api/add-cart.php")
    @FormUrlEncoded
    Call<SignupResponse> addToCart(
            @Field("uid") String uid,
            @Field("pid") String pid,
            @Field("colorid") String colorid,
            @Field("sizeid") String sizeid,
            @Field("qty") String qty,
            @Field("totalamount") String tamt
    );
    @GET("api/cart.php")
    Call<CartList> getCart(@Query("uid") String uid);

    @GET("api/delete-cart.php")
    Call<SignupResponse> deleteProductFromCart(@Query("cartid") String cartid);

    @POST("api/add-address.php")
    @FormUrlEncoded
    Call<SignupResponse> addAddress(
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("pincode") String pincode,
            @Field("address") String address,
            @Field("area") String area,
            @Field("landmark") String landmark,
            @Field("city") String city,
            @Field("state") String state,
            @Field("dtime") String time
    );

    @POST("api/edit-address.php")
    @FormUrlEncoded
    Call<SignupResponse> updateAddress(
            @Field("id") String id,
            @Field("uid") String uid,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("pincode") String pincode,
            @Field("address") String address,
            @Field("area") String area,
            @Field("landmark") String landmark,
            @Field("city") String city,
            @Field("state") String state,
            @Field("dtime") String time
    );

    @GET("api/address.php")
    Call<AddressList> getAddress(@Query("uid") String uid);

    @GET("api/delete-address.php")
    Call<SignupResponse> deleteAddress(@Query("sipid") String id);

    @POST("api/getorder.php")
    @FormUrlEncoded
    Call<SignupResponse> sendOrder(
            @Field("uid") String uid,
            @Field("totalamount") String tamt,
            @Field("totalitem") String titem,
            @Field("paymenttype") String paytype,
            @Field("addressid") String addressid
    );

    @POST("paytm-app/generateChecksum.php")
    @FormUrlEncoded
    Call<ChecksumResponse> generateChecksum(
            @Field("MID") String mid,
            @Field("ORDER_ID") String oid,
            @Field("CUST_ID") String cid,
            @Field("CHANNEL_ID") String channelid,
            @Field("TXN_AMOUNT") String amount,
            @Field("WEBSITE") String web,
            @Field("CALLBACK_URL") String callbakUrl,
            @Field("INDUSTRY_TYPE_ID") String industryid
    );

    @POST("api/businesslogin.php")
    @FormUrlEncoded
    Call<LoginResponse> loginBusiness(
            @Field("uid") String uid,
            @Field("userid") String refid
    );

    @POST("api/businessregister.php")
    @FormUrlEncoded
    Call<LoginResponse> registerBusiness(
            @Field("uid") String uid,
            @Field("referralid") String refid,
            @Field("adharid") String adhaarid,
            @Field("amount") String dpAmt
    );

    @FormUrlEncoded
    @POST("cate/review-post.php")
    Call<SignupResponse> applyRating(@Field("uid") String uid,
                                     @Field("oid") String oid,
                                     @Field("pid") String pid,
                                     @Field("rating") String rate,
                                     @Field("msg") String msg);

    @FormUrlEncoded
    @POST("cate/cancelorder.php")
    Call<SignupResponse> cancelOrder(@Field("cancelid") String cid,
                                     @Field("cancelpid") String cpid,
                                     @Field("canceluid") String cuid,
                                     @Field("creason") String creason,
                                     @Field("cancelmsg") String cmsg,
                                     @Field("canceloid") String coid);

    @FormUrlEncoded
    @POST("cate/returnorder.php")
    Call<SignupResponse> returnOrder(@Field("reid") String rid,
                                     @Field("repid") String rpid,
                                     @Field("reuid") String rrate,
                                     @Field("rereason") String rreason,
                                     @Field("remsg") String rmsg,
                                     @Field("reoid") String roid);
    @GET("api/companyprofile.php")
    Call<SingleResponse> getAboutUs();

    @GET("api/privacyandpolicy.php")
    Call<SingleResponse> getPrivacy();

    @GET("api/termsandconditions.php")
    Call<SingleResponse> getTerms();

    @GET("api/contactus.php")
    Call<ContactUsResponse> getContactInfo();

    @POST("api/add-money.php")
    @FormUrlEncoded
    Call<SignupResponse> addMoney(
            @Field("uid") String uid,
            @Field("amount") String amount
    );

    @GET("api/wallet-amount.php")
    Call<ProfileResponse> getWalletAmount(@Query("uid") String uid);

    @GET("api/mybalance.php")
    Call<BalanceResponse> getMyBalance(@Query("uid") String uid);

    @POST("api/redeem-request.php")
    @FormUrlEncoded
    Call<SignupResponse> redeemRequest(
            @Field("uid") String uid,
            @Field("amount") String amount
    );

    @GET("api/mytransaction.php")
    Call<TransList> getTransaction(@Query("uid") String uid);

    @GET("api/mynetwork.php")
    Call<NetworkList> getNetwork(@Query("referralcode") String refCode);

    @GET("api/vieworder.php")
    Call<OrderList> getOrderList(@Query("uid") String uid);

    @GET("api/vieworderproducts.php")
    Call<OrderDetailsList> getAllOrderList(@Query("orderid") String oid);

    @FormUrlEncoded
    @POST("api/registerfcm.php")
    Call<SignupResponse> registerFcm(@Field("regID") String regId,
                                     @Field("mono") String mono);

    @Multipart
    @POST("api/updprofileimg.php")
    Call<SignupResponse> uploadImg(@Part("uid") String uid,
                                   @PartMap Map<String, RequestBody> map);
    @GET("api/videos.php")
    Call<VideoList> getVideos(@Query("sid") String sid);
    @GET("api/releted.php")
    Call<ReletedVideoList> getReletedVideos(@Query("sid") String sid, @Query("subcat") String subcat);
    @GET("api/trailers.php")
    Call<TrailersList> getTrailer();
    @GET("api/addview.php")
    Call<SignupResponse> Addview(@Query("vid") String vid);

    @GET("api/design-paging.php")
    Call<ProductList> getProductsPaging(@Query("sid") String sid, @Query("pno") Integer pno);
}
