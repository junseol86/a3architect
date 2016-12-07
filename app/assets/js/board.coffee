$ ->

@setPagesInterface = () ->
  pageNumbers = $('#pages_container #pages #numbers .number')
  pagesCount = pageNumbers.length
  pageNoWidth = pageNumbers.width()
  pageNoScroll = $('#pages_container #pages #numbers')

  pageNumbers.click () ->
    $(this).siblings().removeClass '-on'
    $(this).addClass '-on'
    pageMove()

  if (pagesCount <= 5)
    $('#pages_container img').css 'display', 'none'
    $('#pages_container #pages').css 'width', pageNoWidth * pagesCount + 'px'
    $('#pages_container #numbers').css 'width', pageNoWidth * pagesCount + 'px'
  else
    $('#pages_container #pages #numbers #numbers_lined').css 'width', pageNoWidth * pagesCount + 'px'
    console.log(($('#pages_container #pages #numbers .number.-on').text() - 3) * pageNoWidth)
    pageNoScroll.scrollLeft(($('#pages_container #pages #numbers .number.-on').text() - 3) * pageNoWidth)


  $('#pages_container #pages #move_right').click () ->
    pageNoScroll.animate({scrollLeft: '+=' + pageNoScroll.width()}, 300);

  $('#pages_container #pages #move_left').click () ->
    pageNoScroll.animate({scrollLeft: '-=' + pageNoScroll.width()}, 300);

@afterAjaxLoad = (data) ->
  $('#list_container').html(data)
  setPagesInterface()
  $('.board_list_item').click () ->
    location.href = $(this).data('url')
