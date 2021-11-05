const item1 = {
    id: 1,
    image: "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTEwMjhfOTcg%2FMDAxNjM1NDAwMDAzODE0.L57Tge6EjhC10sSDOMe-tlyoPuIddBjkmcCLboN3hQog.FWIsSt3YZOXBev9XpvuBBw6afVmTINjWWWdHR9hvDrAg.PNG.jeterlee%2Fimage.png&type=sc960_832",
    name: "강희정",
    description: "설명입니다",
    price: 30000
}
const item2 = {
    id: 2,
    image: "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMTEwMjhfOTcg%2FMDAxNjM1NDAwMDAzODE0.L57Tge6EjhC10sSDOMe-tlyoPuIddBjkmcCLboN3hQog.FWIsSt3YZOXBev9XpvuBBw6afVmTINjWWWdHR9hvDrAg.PNG.jeterlee%2Fimage.png&type=sc960_832",
    name: "강희정",
    description: "설명입니다",
    price: 30000
}
const storeId = 1;
const resp = {
    data: [item1, item2]
}
let foodItem = {};



$(document).on('ready', function (e) {
    // // 주석
    // $.ajax({
    //     method: "get",
    //     // url: "http://localhost:8080/api/v1/stores/"+[[${storeId}]]+"/foods",
    //     url: "http://localhost:8080/api/v1/stores/"+[[storeId]]+"/foods",
    //     success: function (result) {
    //         console.log("성공 "+result)
    //         output(result)
    //     },
    //     error: function () {
    //         alert("잘못된 정보입니다.");
    //     }
    // })
    // //주석 end

    output(resp);
})

function output(resp) {
    console.log(resp)
    const foodInfoList = resp["data"];
    let result = '';
    $.each(foodInfoList, function (index, item) {
        result += generateFoodComponent(index, item);
        foodItem.push(item);
    })
    $(`#foodList`).html(result)
}

function generateFoodComponent(index, item) {
    let result = '';
    if (index % 3 === 0) result += '<div class="row">'
    result += '<div class="col-lg-4">'
    result += '<img src = "' + item["image"] + '" width="140" height="140">'
    result += '<h2>' + item["name"] + '</h2>'
    result += '<p>' + item["description"] + '</p>'
    result += '<p>' + item["price"] + '원</p>'

    result += '<select id="foodQuantity"'+ item["id"]+' class="form-select foodQuantity" aria-label="Default select example" style="width:auto;">'
    result += '<option selected>갯수</option>'
    result += '<option value="1">1</option>'
    result += '<option value="2">2</option>'
    result += '<option value="3">3</option>'
    result += '</select>'

    result += '<p> <button class="btn btn-secondary orderButton" id="' + item["id"] + '" type="button">주문하기 »</button></p>'
    result += '</div>';
    if (index % 3 === 2 && index !== 0) result += '</div>'
    return result;
}

function bindOrderButton() {
    console.log("test")
    const orderButtons = document.querySelectorAll('.orderButton');
    console.log(orderButtons);
    Array.from(orderButtons).forEach(orderButton => {
        orderButton.addEventListener('click', function (e) {
            console.log("stet");
            let foodId = orderButton.id;
            let foodPrice = foodItem.find(item => item.id === foodId).price;
            let foodQuantity = 1;
            // let foodQuantity = document.querySelector("#foodQuantity"+foodId);
            // let foodQuantity = $("#foodQuantity option:selected").val();
            console.log("id, price,quantity "+foodId+" "+foodPrice+" "+foodQuantity);
            // $.ajax({
            //     type: "post",
            //     url: "http://localhost:8080/stores/"+[[storeId]]+"/orderFood",
            //     // url: "http://localhost:8080/stores/"+[[${storeId}]]+"/orderFood",
            //     data: {
            //         id: foodId,
            //         price: foodPrice,
            //         quantity : foodQuantity
            //     },
            //     success: function (result) {
            //         console.log("성공")
            //     },
            //     error: function () {
            //         alert("잘못된 정보입니다.");
            //     }
            // })
        });
    });

}

function render() {
    bindOrderButton();
}

document.addEventListener('DOMContentLoaded', function () {
    render();
});
