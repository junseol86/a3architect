$ ->
#  시간 출력 프로토타입
  Date.prototype.getTimeString = ->
    timeString = this.getFullYear() + "-"
    timeString += this.getMonth() + "-"
    timeString += this.getDate() + " "
    timeString += this.getHours() + ":"
    timeString += this.getMinutes() + ":"
    timeString += this.getSeconds()
    timeString

  # 우상단 로그인 기능
  $('#account .login_btn').click ->
    $('.login_interface').css 'display', 'block'
    $('.login_and_register').css 'display', 'none'
    $('#login_id_input').focus()

  $('#account .login_cancel').click ->
    $('#account .login_interface').css 'display', 'none'
    $('#account .login_and_register').css 'display', 'block'
    $('#account #login_id_input').val ''
    $('#account #login_pw_input').val ''

  $('#account .login_ok').click ->
    loginSubmit()

  $('#account #login_pw_input').keyup (e) ->
    if (e.keyCode == 13)
      loginSubmit()

  $('#account .logout_btn').click ->
    $.post '/logout',
      {
      }
      (data, status) ->
        location.reload()

#  상단 돋보기 클릭시 입력창 포커스
  $('.top #account img').click ->
    if $('.top #account input').val().trim() == ''
      $('.top #account input').focus()

#  네비게이션 메뉴 클릭시 페이지 이동
  $('#navigate li').click ->
    if ($(this).hasClass('secured') && $('#i_am_logged_in').length < 1)
      alert '로그인이 필요합니다.'
    else
      category = $(this).closest('div').attr('id')
      page = $(this).attr('id')
      navigate('/' + category + '/' + page)

  # 컨텐츠 화면 최소높이
  $('.content_wrapper').css 'min-height', ($(window).height() - $('.top').height() - $('.footer_wrapper').height()) + 'px'

  $('.filters .items > div').click ->
    $(this).parent('div').addClass '-on'
    $('.filters .items  div').removeClass '-on'
    $(this).addClass '-on'

  $('.filters #all').click ->
    $('.filters .items').removeClass '-on'
    $('.filters .items  div').removeClass '-on'

  $('.on_only_one > *').click ->
    $(this).siblings('*').removeClass '-on'
    $(this).addClass '-on'

#  클릭하여 -on 토글되는 객체들
  $('.on_off_toggle').click ->
    if $(this).hasClass '-on'
      $(this).removeClass '-on'
    else
      $(this).addClass '-on'


#페이지 이동 (뒤로가기 가능)
@navigate = (link) ->
  window.location.href = link
  false

#페이지 이동 (뒤로가기 불가)
@goto = (link) -> window.location.replace(link)

#네비게이션 메뉴에 현 페이지 마크
@navigationMark = (category, page) ->
  $('#' + category).addClass '-on'
  $('#' + category + ' #' + page).addClass '-on'

@loginSubmit = ->
  $.post '/login',
    {
      id: $('#login_id_input').val()
      pw: $('#login_pw_input').val()
    }
    (data, status) ->
      console.log data
      dataJson = JSON.parse(data)
      if (dataJson["logged_in"] == 1)
        location.reload()
      else
        alert "아이디와 비밀번호를 확인하세요."
