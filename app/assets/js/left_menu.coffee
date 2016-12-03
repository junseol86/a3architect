$ ->
#  leftMenuVal 값은 _A_admin_left_menu 파일에서
  $('.left_menu > div').each () ->
    if ($(this).attr('id') == leftMenuVal)
      $(this).addClass('-on')

  $('.left_menu > div').click () ->
    location.href = '/admin/' + $(this).attr('id')
