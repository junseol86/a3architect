$ ->
#  상단 돋보기 클릭시 입력창 포커스
  $('.top #account img').on 'click', ->
    if $('.top #account input').val().trim() == ''
      $('.top #account input').focus()

#  네비게이션 메뉴 클릭시 페이지 이동
  $('#navigate li').on 'click', event, ->
    category = event.target.closest('div').id
    page = event.target.id
    navigate('/' + category + '/' + page)

  $('.content_wrapper').css 'min-height', ($(window).height() - $('.top').height() - $('.footer_wrapper').height()) + 'px'

#페이지 이동 (뒤로가기 가능)
navigate = (link) -> window.location.href = link

#페이지 이동 (뒤로가기 불가)
goto = (link) -> window.location.replace(link)

#네비게이션 메뉴에 현 페이지 마크
@navigationMark = (category, page) ->
  $('#' + category).addClass('-on')
  $('#' + category + ' #' + page).addClass('-on')