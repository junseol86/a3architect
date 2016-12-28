$ ->
  $('.toggle_sts').click () ->
    userId = $(this).attr('id')
    $.post '/admin/user_sts_toggle',
      {
        id: userId
      }
      (data, status) ->
        if (data == 'I')
          $('#' + userId).text 'ON'
          $('#' + userId).css 'color', 'dodgerblue'
        else
          $('#' + userId).text 'OFF'
          $('#' + userId).css 'color', 'tomato'


