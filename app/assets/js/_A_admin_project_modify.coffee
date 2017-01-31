$ ->
#  주용도, 주구조 선택장 내용 받아오기
  $('#yongdo_main').change ->
    $('#yongdo_input').val $(this).val()

  $('#gujo_main').change ->
    $('#gujo_input').val $(this).val()

  $('#project_write_confirm').click ->
    items = []
    $('input.number_only').each () ->
      if ($(this).val().charAt(0) == '.')
        $(this).val('0' + $(this).val())
      if ($(this).val().charAt($(this).val().length - 1) == '.')
        $(this).val($(this).val() + '0')
      if (!Number($(this).val()))
        items.push $(this).data('item')
    if (items.length == 0)
      pfm.projectSubmit()
    else
      alert items + '에는 숫자만 입력하세요.'

  $('.write_child_btn').click ->
    location.href = $(this).data('url')

@projectFormManager = () ->
  {
    "projectSubmit": () ->
      $('.project #modified_input').val(new Date().getTimeString())
      category = 0
      $.each $('.circles > div'), (idx, circle) ->
        if $(circle).hasClass('-on')
          category = idx + 1
      $('.project #category_input').val(category)
      document.project_write.submit()
  }
