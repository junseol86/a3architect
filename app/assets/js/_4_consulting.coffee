$ ->
  for x in [1..4]
    $('#process_' + x).css 'background-image', "url('/assets/images/interface/process_bg_" + x + ".jpg')"
    image_process '#process_' + x

@findAddress = (num) ->
  new daum.Postcode({
    oncomplete: (data) ->
      $('#address_' + num + ' input').val(data.roadAddress)
#      http://postcode.map.daum.net/guide
  }).open()
