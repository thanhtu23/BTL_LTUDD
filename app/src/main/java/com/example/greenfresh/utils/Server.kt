package com.example.greenfresh.utils

class Server {
    companion object {


        // IP address
        // ipconfig => ipv4
        val localhost = "192.168.100.11"
        //

            val linkLogin = "http://$localhost/greenfresh/login.php"
        val linkRegister = "http://$localhost/greenfresh/signup.php"

        val linkBanner = "http://$localhost/greenfresh/banner.php"
        val linkCategory="http://$localhost/greenfresh/category.php"
        val linkSeller="http://$localhost/greenfresh/getProductSeller.php"
        val linkProduct="http://$localhost/greenfresh/product.php"

            val linkAddCart = "http://$localhost/greenfresh/addCart.php"
        val linkGetCart = "http://$localhost/greenfresh/getCart.php"

        val linkDeleteCart = "http://$localhost/greenfresh/deleteCart.php"
        val linkUpdateNumCart = "http://$localhost/greenfresh/updatenum.php"

        val linkUser = "http://$localhost/greenfresh/getUser.php"

        val linkChangePass = "http://$localhost/greenfresh/changePass.php"
        val linkOrder = "http://$localhost/greenfresh/order.php"
        val linkOrderDetail = "http://$localhost/greenfresh/getOrder.php"
        val linkNotifi = "http://$localhost/greenfresh/notification.php"
    }
}