$ ->
#  주용도, 주구조 선택장 내용 받아오기
  $.each pfm.getYongdos(), (idx, yongdo) ->
    $('#yongdo_main').append '<option value="' + yongdo + '">' + yongdo + '</option>'
  $('#yongdo_main').change ->
    yongdo = $(this).val()
    $('#yongdo_sub').css('display', 'block').html ""
    $.each pfm.getSubYongdos().filter((sy) -> sy['yongdo'] == yongdo), (idx, subYongdo) ->
      $('#yongdo_sub').append '<option value="' + subYongdo['subYongdo'] + '">' + subYongdo['subYongdo'] + '</option>'
  $.each pfm.getGujos(), (idx, gujo) ->
    $('#gujo_main').append '<option value="' + gujo + '">' + gujo + '</option>'
  $('#gujo_main').change ->
    gujo = $(this).val()
    $('#gujo_sub').css('display', 'block').html ""
    $.each pfm.getSubGujos().filter((sy) -> sy['gujo'] == gujo), (idx, subGujo) ->
      $('#gujo_sub').append '<option value="' + subGujo['subGujo'] + '">' + subGujo['subGujo'] + '</option>'

  $('.project #submit_button_container > div').click ->
    pfm.conApplySubmit()
    
@projectFormManager = () ->
  yongdos = []
  subYongdos = []
  gujos = []
  subGujos = []

  {
    "getYongdos": () ->
      yongdos
    "getSubYongdos": () ->
      subYongdos
    "addYongdos": (yongdoName, subYongdoName) ->
      yongdo = {
        "yongdo": yongdoName,
        "subYongdo": subYongdoName
      }
      subYongdos.push(yongdo)
      if ($.inArray(yongdoName, yongdos) == -1)
        yongdos.push(yongdoName)

    "getGujos": () ->
      gujos
    "getSubGujos": () ->
      subGujos
    "addGujos": (gujoName, subGujoName) ->
      gujo = {
        "gujo": gujoName,
        "subGujo": subGujoName
      }
      subGujos.push(gujo)
      if ($.inArray(gujoName, gujos) == -1)
        gujos.push(gujoName)

    "projectSubmit": () ->
      $('.project #created_input').val(new Date().getTimeString())
      category = 0
      $.each $('.circles > div'), (idx, circle) ->
        if $(circle).hasClass('-on')
          category = idx + 1
      $('.project #category_input').val(category)
      document.project_write.submit()
  }
