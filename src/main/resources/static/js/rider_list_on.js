 window.addEventListener('load', function() {

            // 주문 완료 메세지 받기
            const socket = new SockJS('/websocket');
            const stompClient = Stomp.over(socket);
            stompClient.connect({}, function() {

            const storeId = '63';
            stompClient.subscribe('/topic/order-complete/'+storeId, function(message) {

            const messageBody = JSON.parse(message.body);

            const orders = messageBody.a;
            const orderId = orders.ordersId;
<!--  ------------------------------------------------------------------------------------------ -->

            <!-- 배달 지역 -->
            const address = orders.ordersAddress;
            console.log(address);
            var parts = address.split(" ")[1];
            console.log(parts);

            <!-- 라이더 희망지역 -->
            const riderArea = $("#workingArea").val();

            var headers = {};
            headers["appKey"]="4jevlbxAGy2TQzNvpdD2B3eAfmkUdXQr8uWsi1A1";

            var endX = '';
            var endY = '';

            if(riderArea === parts){
            $.ajax({
            method: "GET",
            headers: headers,
            url: "https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?version=1&format=json",
            async: false,
            data: {
                "coordType": "WGS84GEO",
                "fullAddr": address
            },
            success: function(response) {
                var resultInfo = response.coordinateInfo;
                console.log(resultInfo);
                console.log("위도: " + resultInfo.coordinate[0].newLatEntr);
                console.log("경도: " + resultInfo.coordinate[0].newLonEntr);

                console.log(resultInfo.coordinate[0].lon);
                console.log(resultInfo.coordinate[0].lat);

                console.log(resultInfo.coordinate[0].adminDong);

                    if (resultInfo.coordinate[0].adminDong === "" || resultInfo.coordinate[0].adminDong === null) {
                        endX = resultInfo.coordinate[0].newLonEntr;
                        endY = resultInfo.coordinate[0].newLatEntr;
<!--                        distance(endY, endX);-->
                    }else {
                        endX = resultInfo.coordinate[0].lon;
                        endY = resultInfo.coordinate[0].lat;
<!--                        distance(endY, endX);-->
                    }
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });
        console.log(endY);
        console.log(endX);

        navigator.geolocation.getCurrentPosition((position) => {

                    console.log(position.coords.latitude);
                    console.log(position.coords.longitude);
                    var searchOption = "12";

                    var trafficInfochk = "N";

                    var headers = {};
                    headers["appKey"]="4jevlbxAGy2TQzNvpdD2B3eAfmkUdXQr8uWsi1A1";

                    $.ajax({
                    type : "POST",
                    headers : headers,
                    url : "https://apis.openapi.sk.com/tmap/routes?version=1&format=json&callback=result&appKey=4jevlbxAGy2TQzNvpdD2B3eAfmkUdXQr8uWsi1A1",
                    async : false,
                    data : {
                         "startX" : position.coords.longitude,
                         "startY" : position.coords.latitude,
                         "endX" : endX,
                         "endY" : endY,
                         "reqCoordType" : "WGS84GEO",
                         "resCoordType" : "EPSG3857",
                         "searchOption" : searchOption,
                         "trafficInfo" : trafficInfochk
                    },
                    success : function(response) {

                     var resultData = response.features;

                     var tDistance = "거리 : " + (resultData[0].properties.totalDistance / 1000)
                                 .toFixed(1) + "km";
                    console.log(tDistance);
                    $('.delivery_dis').eq(0).html(tDistance);

                     var tTime = " 총 시간 : "
                           + (resultData[0].properties.totalTime / 60)
                                 .toFixed(0) + "분";
                    }
                });
            });
        }



<!--  ------------------------------------------------------------------------------------------ -->

            $(".drive_none").hide();

            var listItem = '' ;

            if(orders == null){
            listItem =`
                    <li class='drive_none'>
                        <span>현재 배달 요청이 없습니다!</span>
                    </li>`;
            }else if(riderArea === parts){
                var count = parseInt($(".ride_num").text(), 10);
                count++;
                $(".ride_num").text(count + "건");

                listItem = `
                    <a href='/ride/orders/25'>
                        <li>
                            <span class='order_num'>주문번호 :&nbsp;</span><span class='order_num' name="ordersId" >1119</span><br>
                            <span class='delivery_address'>${orders.ordersAddress} ${orders.ordersAddressDetails}</span>
                            <div class='space'>
                                <span class='delivery_time'>${orders.ordersDate}</span>
                                <span class='delivery_dis'>100km</span>
                            </div>
                        </li>
                    </a>`;
                }
                $("#orderList").prepend(listItem);
                });
            });
        });


        function distance(endY, endX){
            navigator.geolocation.getCurrentPosition((position) => {

                    console.log(position.coords.latitude);
                    console.log(position.coords.longitude);
                    var searchOption = "12";

                    var trafficInfochk = "N";

                    var headers = {};
                    headers["appKey"]="4jevlbxAGy2TQzNvpdD2B3eAfmkUdXQr8uWsi1A1";

                    $.ajax({
                    type : "POST",
                    headers : headers,
                    url : "https://apis.openapi.sk.com/tmap/routes?version=1&format=json&callback=result&appKey=4jevlbxAGy2TQzNvpdD2B3eAfmkUdXQr8uWsi1A1",
                    async : false,
                    data : {
                         "startX" : position.coords.longitude,
                         "startY" : position.coords.latitude,
                         "endX" : endX,
                         "endY" : endY,
                         "reqCoordType" : "WGS84GEO",
                         "resCoordType" : "EPSG3857",
                         "searchOption" : searchOption,
                         "trafficInfo" : trafficInfochk
                    },
                    success : function(response) {

                     var resultData = response.features;

                     var tDistance = "거리 : " + (resultData[0].properties.totalDistance / 1000)
                                 .toFixed(1) + "km";
                    console.log(tDistance);
                    $('.delivery_dis').eq(0).html(tDistance);

                     var tTime = " 총 시간 : "
                           + (resultData[0].properties.totalTime / 60)
                                 .toFixed(0) + "분";
                    }
                });
            });
        }