#회원가입시 input 컨트로를 위한 모듈
$ ->
  ivm = new inputValidModule()

  userIdInput = $('.register #user_id input')
  userIdIndicator = $('.register #user_id span')
  userIdInput.change () ->
    ivm.indicateValidState(userIdInput.val(), '영문 + 숫자', ivm.validId, userIdIndicator)
    idCheckBtn.css 'display', 'block'

  userPwInput = $('.register #user_pw input')
  userPwIndicator = $('.register #user_pw span')
  userPwInput.keyup () ->
    ivm.indicateValidState(userPwInput.val(), '6자 이상 영문 + 숫자', ivm.validPassword, userPwIndicator)

  userPwConfirmInput = $('.register #user_pw_confirm input')
  userPwConformIndicator = $('.register #user_pw_confirm span')
  userPwConfirmInput.keyup () ->
    ivm.indicateValidPasswordConfirm(userPwInput.val(), userPwConfirmInput.val(), userPwConformIndicator)

  #아이디 중복확인
  idCheckBtn = $('#id_check')
  idCheckBtn.click () ->
    if (userIdInput).val().length == 0
      alert '아이디를 입력하세요.'
      return
    $.post '/user/id_check',
      {
        id: userIdInput.val()
      }
      (data, status) ->
        console.log data
        if (data == '1')
          alert "일치하는 아이디가 있습니다."
          userIdInput.val('')
        else
          alert "사용할 수 있는 아이디입니다."
          idCheckBtn.css 'display', 'none'

  #회원가입 제출
  registerSubmitBtn = $('.register #submit_button_container div')
  registerSubmitBtn.click () ->

    if (userIdInput.val().length < 1)
      return alert '아이디를 입력하세요'

    if (idCheckBtn.css('display') != 'none')
      return alert '아이디 중복확인을 해주세요.'

    idCheck = ivm.validId(userIdInput.val())
    if (idCheck != 'OK')
      return alert(idCheck)

    pwCheck = ivm.validPassword(userPwInput.val())
    if (pwCheck != 'OK')
      return alert('비밀번호를' + pwCheck)

    pwConfirmCheck = ivm.validPasswordConfirm(userPwInput.val(), userPwConfirmInput.val())
    if (pwConfirmCheck != true)
      return alert('비밀번호를 일치하게 입력해주세요.')

    requiredFields = $('.register .required')
    for i in [0..(requiredFields.length - 1)]
      if ($(requiredFields[i]).val().length == 0)
        return alert $(requiredFields[i]).data('name') + '을(를) 입력해주세요.'

    $('.register #created_input').val(new Date().getTimeString())
    document.register_form.submit()


@letterAndNumber = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
@letters = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
@numbers = '0123456789'

@inputValidModule = () -> {

  validId: (input) ->
    for i in [0..input.length]
      if (letterAndNumber.indexOf(input.charAt(i)) < 0)
        return '영문과 숫자만 입력하세요!'
    return 'OK'

  validPassword: (input) ->
    if (input.length < 6)
      return '6자 이상 입력하세요.'
    else
      hasLetter = false
      hasNumber = false
      for i in [0..input.length]
        if (letterAndNumber.indexOf(input.charAt(i)) < 0)
          return '영문과 숫자만 입력하세요!'
        else
          if (letters.indexOf(input.charAt(i)) < 0)
            hasLetter = true
          if (numbers.indexOf(input.charAt(i)) < 0)
            hasNumber = true
      if (hasLetter && hasNumber)
        return 'OK'
      else
        '영문과 숫자 모두 포함하세요'

  validPasswordConfirm: (pw, confirm) ->
    return pw == confirm

  indicateValidState: (input, whenEmpty, validFunction, indicator) ->
    if (input.length == 0)
      indicator.text whenEmpty
      indicator.css 'color', '#bfbfbf'
    else
      result = validFunction.call(undefined , input)
      indicator.text(result)
      if (result == 'OK')
        indicator.css 'color', 'dodgerblue'
      else
        indicator.css 'color', 'tomato'

  indicateValidPasswordConfirm: (pw, confirm, indicator) ->
    if (confirm.length == 0)
      indicator.text ''
    else if (pw == confirm)
      indicator.text 'OK'
      indicator.css 'color', 'dodgerblue'
    else
      indicator.text '비밀번호와 일치하지 않습니다.'
      indicator.css 'color', 'tomato'
}

