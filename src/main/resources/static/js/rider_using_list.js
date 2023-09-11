 window.addEventListener('load', function(){
                        var fullAddressElements = document.querySelectorAll('#delivery_address');

                        // 값을 담을 빈 배열을 생성합니다.
                        var addresses = [];

                        // 각 요소의 값을 배열에 추가합니다.
                        fullAddressElements.forEach(function(element) {
                            addresses.push(element.value);
                        });
                        console.log(addresses);

                        var headers = {};
                        headers["appKey"]="4jevlbxAGy2TQzNvpdD2B3eAfmkUdXQr8uWsi1A1";

                    var endX = '';
                    var endY = '';

                addresses.forEach(function(fullAddr, index) {
                        $.ajax({
                            method: "GET",
                            headers: headers,
                            url: "https://apis.openapi.sk.com/tmap/geo/fullAddrGeo?version=1&format=json",
                            async: false,
                            data: {
                                "coordType": "WGS84GEO",
                                "fullAddr": fullAddr
                            },
                            success: function(response) {
                                var resultInfo = response.coordinateInfo;
                                console.log(resultInfo);
                                console.log(resultInfo.coordinate);
                                console.log("주소: " + fullAddr);
                                console.log("위도: " + resultInfo.coordinate[0].newLatEntr);
                                console.log("경도: " + resultInfo.coordinate[0].newLonEntr);

                                endX = resultInfo.coordinate[0].newLonEntr;
                                endY = resultInfo.coordinate[0].newLatEntr;

                                distance(endY, endX, index);
                            },
                            error: function(xhr, status, error) {
                                console.error(error);
                            }
                        });
                    });
            console.log(endX);
            console.log(endY);
        });



        function distance(endY, endX, index){
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
                    $('.delivery_dis').eq(index).html(tDistance);

                     var tTime = " 총 시간 : "
                           + (resultData[0].properties.totalTime / 60)
                                 .toFixed(0) + "분";
                    }
                });
            });
        }