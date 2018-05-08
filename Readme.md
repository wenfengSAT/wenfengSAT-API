## Explore Rest APIs

使用Postman测试：
设置Header:Content-Type:application/json  （application/x-www-form-urlencoded）

    1、POST http://localhost:8080/tokens/login
                    通过登陆接口获取有一个token
    2、调用其他接口时把token设置在头信息上（设置Header：token：token号）