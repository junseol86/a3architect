$ ->

@setPagesInterface = () ->
  pageNumbers = $('#pages_container #pages #numbers .number')
  pagesCount = pageNumbers.length
  pageNoWidth = pageNumbers.width()
  pageNoScroll = $('#pages_container #pages #numbers')

  pageNumbers.click () ->
    location.hash = ''
    $(this).siblings().removeClass '-on'
    $(this).addClass '-on'
    pageMove()

  if (pagesCount <= 5)
    $('#pages_container img').css 'display', 'none'
    $('#pages_container #pages').css 'width', pageNoWidth * pagesCount + 'px'
    $('#pages_container #numbers').css 'width', pageNoWidth * pagesCount + 'px'
  else
    $('#pages_container #pages #numbers #numbers_lined').css 'width', pageNoWidth * pagesCount + 'px'
    pageNoScroll.scrollLeft(($('#pages_container #pages #numbers .number.-on').text() - 3) * pageNoWidth)


  $('#pages_container #pages #move_right').click () ->
    pageNoScroll.animate({scrollLeft: '+=' + pageNoScroll.width()}, 300);

  $('#pages_container #pages #move_left').click () ->
    pageNoScroll.animate({scrollLeft: '-=' + pageNoScroll.width()}, 300);

@afterAjaxLoad = (data) ->
  $('#list_container').html(data)
  setPagesInterface()
  $('.board_list_item').click () ->
    location.hash = getCurrentPage()
    location.href = $(this).data('url')

@getCurrentPage = () ->
#  해시를 없앴을 때 타 브라우저는 '', 익스플로러는 '#'을 남기므로 제거한다.
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[0]
  else
    pageOn = $('#pages_container #pages #numbers .number.-on')
    if pageOn.length then pageOn.text() else 1
