$( function() {
  $(".bucket-count").text(cookkies("bucket").split("-").length)
} );
function cookkies(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  var filters = ca.filter(function (x) {
    return x.match(cname+"=")
  });
  if(filters.length === 0) {
    return "";
  } else {
    return filters[0].split("=")[1]
  }
}