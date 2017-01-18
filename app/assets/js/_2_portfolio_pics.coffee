$ ->
  $('#pics_right').click ->
    $('#pics').animate({scrollLeft: '+=' + 560}, 300);
  $('#pics_left').click ->
    $('#pics').animate({scrollLeft: '-=' + 560}, 300);
