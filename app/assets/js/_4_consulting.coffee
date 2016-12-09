$ ->
#  진행절차 화면 배경 사이즈 조절
  for x in [1..4]
    $('#process_' + x).css 'background-image', "url('/assets/images/interface/process_bg_" + x + ".jpg')"
    imageProcessor().image_process '#process_' + x

  $('.circles > div').click ->
    $('.form #title').text $(this).find('h1').text().replace(' 컨설팅', '') + ' 컨설팅 신청'

#  필지 추가
  $('#address_add_2').click ->
    $(this).css 'display', 'none'
    $('#address_2').css 'display', 'block'
    $('#address_add_3').css 'display', 'block'
  $('#address_add_3').click ->
    $(this).css 'display', 'none'
    $('#address_3').css 'display', 'block'

  $('.as_apply #submit_button_container > div').click ->
    asApplyFormManager().asApplySubmit()


# 다음 API 이용하여 주소 찾기
@findAddress = (num) ->
  new daum.Postcode({
    oncomplete: (data) ->
      $('#address_' + num + ' input').val(data.roadAddress)
#      http://postcode.map.daum.net/guide
  }).open()

@asApplyFormManager = () ->
  {
    "asApplySubmit": () ->
      $('.as_apply #created_input').val(new Date().getTimeString())
      document.as_apply_form.submit()
  }
