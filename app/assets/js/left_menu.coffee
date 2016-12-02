$ ->
  $('.left_menu > div').click () ->
    location.href = '/admin/' + $(this).attr('id')

@leftMenuMark = (leftMenu) ->
  $('.left_menu > div').each () ->
    if ($(this).attr('id') == leftMenu)
      $(this).addClass('-on')
