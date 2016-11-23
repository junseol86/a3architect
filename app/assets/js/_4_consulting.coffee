$ ->
  for x in [1..4]
    $('#process_' + x).css 'background-image', "url('/assets/images/interface/process_bg_" + x + ".jpg')"
    image_process '#process_' + x

  $('#address_add_2').click () ->
    $(this).css 'display', 'none'
    $('#address_2').css 'display', 'block'
    $('#address_add_3').css 'display', 'block'

  $('#address_add_3').click () ->
    $(this).css 'display', 'none'
    $('#address_3').css 'display', 'block'

  $.each yongdos, (idx, yongdo) ->
    $('#yongdo_main').append '<option>' + yongdo + '</option>'
  $('#yongdo_main').change () ->
    yongdo = $(this).val()
    $('#yongdo_sub').css('display', 'block').html("")
    $.each subYongdos.filter((sy) -> sy['yongdo'] == yongdo), (idx, subYongdo) ->
      $('#yongdo_sub').append '<option>' + subYongdo['subYongdo'] + '</option>'


  $.each gujos, (idx, gujo) ->
    $('#gujo_main').append '<option>' + gujo + '</option>'
  $('#gujo_main').change () ->
    gujo = $(this).val()
    $('#gujo_sub').css('display', 'block').html("")
    $.each subGujos.filter((sy) -> sy['gujo'] == gujo), (idx, subGujo) ->
      $('#gujo_sub').append '<option>' + subGujo['subGujo'] + '</option>'

@findAddress = (num) ->
  new daum.Postcode({
    oncomplete: (data) ->
      $('#address_' + num + ' input').val(data.roadAddress)
#      http://postcode.map.daum.net/guide
  }).open()

@yongdos = []
@subYongdos = []
@gujos = []
@subGujos = []

@addYongdos = (yongdoName, subYongdoName) ->
  yongdo = {
    "yongdo": yongdoName,
    "subYongdo": subYongdoName
  }
  subYongdos.push yongdo
  if $.inArray(yongdoName, yongdos) == -1
    yongdos.push yongdoName

@addGujos = (gujoName, subGujoName) ->
  gujo = {
    "gujo": gujoName,
    "subGujo": subGujoName
  }
  subGujos.push gujo
  if $.inArray(gujoName, gujos) == -1
    gujos.push gujoName

