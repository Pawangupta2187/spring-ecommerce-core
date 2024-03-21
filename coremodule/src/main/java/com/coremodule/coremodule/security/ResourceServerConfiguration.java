package com.coremodule.coremodule.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    AppUserDetailsService userDetailsService;



    public ResourceServerConfiguration() {
        super();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }


    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http

                .authorizeRequests()
                .antMatchers("/register/customer","/register/seller","/login","/forgetpassword","/register/activateaccount","/register/resendactivationlink","/changepassword","/register/uploadimage").permitAll()
                .antMatchers("/admin/customers","/admin/sellers",
                        "/admin/activatecustomer/{id}","/admin/deactivatecustomer/{id}",
                        "/admin/activateseller/{id}","/admin/deactivateseller/{id}","/category/admin/addcategory","/category/admin/addmetafield","/categoryadmin/getallmetafield","/category/admin/addsubcategory","/category/admin/getcategory","/category/admin/getallcategory","/category/admin/updatecategory/{id}","/categoryadmin/addCategoryMetaFieldValues",
                        "/category/admin/updatemetadatafieldvalues",
                        "/admin/viewproduct","/admin/viewallproducts","/admin/deactivateproduct/{id}","/admin/activateproduct/{id}","/order/admin/allorders","order/admin/status/{orderProductId}").hasAnyRole("ADMIN")
                .antMatchers("/user/home","/customer/profile","/customer/addresses","/customer/addadress","/customer/addadress/{addressId}",
                        "/category/customer/viewcategory","category/customer/filteroption","/customer/viewproduct","/customer/viewcategoryallproduct",
                        "/customer/similarproduct","/cart/{variationId}","order/orderfromcart","order/orderfromcart/{variationId}","/order/{variationId}","order/cancelorder/{orderProductId}","order/returnorder/{orderProductId}","order/{orderId}").hasAnyRole("CUSTOMER")
                .antMatchers("/seller/profile","/seller/address","seller/password","seller/addproduct","seller/addvariation","/seller/viewproduct","/seller/viewproductvariation","/seller/viewallproduct","seller/viewproductallvariation","/seller/deleteproduct/{id}",
                        "/seller/updateproduct/{id}","/seller/updatevariation/{id}","/category/seller/viewcategory","order/seller/status/{orderProductId}","order/seller/allorder").hasAnyRole("SELLER")
                .antMatchers("/doLogout").hasAnyRole("ADMIN","CUSTOMER","SELLER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and()
                .csrf().disable();



    }

//    @Bean
//    public CustomLoginSuccessHandler successHandler() {
//        return new CustomLoginSuccessHandler();
//    }
//
//    @Bean
//    public CustomLoginFailureHandler failureHandler() {
//        return new CustomLoginFailureHandler();
//    }

}