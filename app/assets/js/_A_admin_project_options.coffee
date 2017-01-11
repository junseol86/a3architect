$ ->
#  주용도, 주구조 선택장 내용 받아오기
  $('#yongdo_main').change ->
    $('#yongdo_input').val $(this).val()

  $('#gujo_main').change ->
    $('#gujo_input').val $(this).val()

  $('#project_write_confirm').click ->
    pfm.projectSubmit()

@projectFormManager = () ->
  {
    "projectSubmit": () ->
      $('.project #created_input').val(new Date().getTimeString())
      category = 0
      $.each $('.circles > div'), (idx, circle) ->
        if $(circle).hasClass('-on')
          category = idx + 1
      $('.project #category_input').val(category)
      document.project_write.submit()
  }
