$ ->
  #  주용도, 주구조 선택장 내용 받아오기
  $.each cafm.getYongdos(), (idx, yongdo) ->
    $('#yongdo_main').append '<option value="' + yongdo + '">' + yongdo + '</option>'
  $('#yongdo_main').change ->
    yongdo = $(this).val()
    $('#yongdo_sub').css('display', 'block').html ""
    $.each cafm.getSubYongdos().filter((sy) -> sy['yongdo'] == yongdo), (idx, subYongdo) ->
      $('#yongdo_sub').append '<option value="' + subYongdo['subYongdo'] + '">' + subYongdo['subYongdo'] + '</option>'
  $.each cafm.getGujos(), (idx, gujo) ->
    $('#gujo_main').append '<option value="' + gujo + '">' + gujo + '</option>'
  $('#gujo_main').change ->
    gujo = $(this).val()
    $('#gujo_sub').css('display', 'block').html ""
    $.each cafm.getSubGujos().filter((sy) -> sy['gujo'] == gujo), (idx, subGujo) ->
      $('#gujo_sub').append '<option value="' + subGujo['subGujo'] + '">' + subGujo['subGujo'] + '</option>'

  $('.con_apply #submit_button_container > div').click ->
      cafm.conApplySubmit()


  #  '계약 스토리'에서 둥근 버튼 클릭시 해당 카테고리로 이동
  $.each $('.contract_story .circles > div'), (idx, circle) ->
    if $('#category_indicator').text() == (idx + 1).toString()
      $(circle).addClass('-on')
    $(circle).click ->
      navigate("/consulting/contract_story/#{idx + 1}/@/1")

@conApplyFormManager = () ->
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

    "conApplySubmit": () ->
      $('.con_apply #created_input').val(new Date().getTimeString())
      category = 0
      $.each $('.circles > div'), (idx, circle) ->
        if $(circle).hasClass('-on')
          category = idx + 1
      $('.con_apply #category_input').val(category)
      document.con_apply_form.submit()
  }
