$ ->
  $('.left_menu > div').click () ->
    location.href = '/admin/' + $(this).attr('id')

@leftMenuMarkModule = () ->
  {
    "mark": (leftMenuVal) ->
      $('.left_menu > div').each () ->
        if ($(this).attr('id') == leftMenuVal)
          $(this).addClass('-on')
  }
