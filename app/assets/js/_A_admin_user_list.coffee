$ ->
  $('.toggle_sts').click () ->
    userId = $(this).attr('id').toString().replace('sts_', '')
    $.post '/admin/user_sts_toggle',
      {
        id: userId
      }
      (data, status) ->
        if (data == 'I')
          $('#sts_' + userId).text 'ON'
          $('#sts_' + userId).css 'color', 'dodgerblue'
        else
          $('#sts_' + userId).text 'OFF'
          $('#sts_' + userId).css 'color', 'tomato'

  $('.pw_reset').click () ->
    userId = $(this).attr('id').toString().replace('pw_reset_', '')
    if (confirm("#{userId}님의 비밀먼호가 \'0000\'으로 초기화됩니다.  진행하시겠습니까?"))
      $.post '/admin/user_pw_reset',
        {
          id: userId
        }
        (data, status) ->
          alert data

  $('.delete').click () ->
    userId = $(this).attr('id').toString().replace('delete_', '')
    if (confirm("#{userId}님의 계정을 완전히 삭제합니다.  일시적인 정지는 '활성'란을 'OFF'해놓으시는 것으로 가능합니다.  계정을 삭제하시겠습니까?"))
      $.post '/admin/user_delete',
        {
          id: userId
        }
        (data, status) ->
          if (data == 'success')
            alert '삭제되었습니다.'
            location.reload()
          else
            alert '실패했습니다.'
