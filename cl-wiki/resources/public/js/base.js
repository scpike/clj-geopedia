function fillCurrentPosition(position){
  if ($("form#geo").length){
    var lat = position.coords.latitude;
    var lon = position.coords.longitude;
    $("form#geo #lat").val(lat);
    $("form#geo #lon").val(lon);
  }
}

$(document).ready(function(){
  if ($("form#geo").length){
    navigator
      .geolocation
      .getCurrentPosition(fillCurrentPosition);
  }
})
