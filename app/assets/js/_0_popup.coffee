$ ->
  $(window).resize ->
    popup = $('#popup')
    black_bg = $('#black_bg')
    winW = $(window).width()
    winH = $(window).height()

    popup.css 'width', winW + 'px'
    popup.css 'height', winH + 'px'
    black_bg.css 'width', winW + 'px'
    black_bg.css 'height', winH + 'px'

    pic = $('#popup #pic')
    pic.css 'top', ((winH - pic.height())/2) + 'px'
    pic.css 'left', ((winW - pic.width())/2) + 'px'

@showPopup = (url) ->
  popup = $('#popup')
  winW = $(window).width()
  winH = $(window).height()

  popup.css 'display', 'block'
  popup.css 'width', winW + 'px'
  popup.css 'height', winH + 'px'

  imageOrg = new Image()
  imageOrg.src = url
  imageOrg.onload = () ->
    imgW = imageOrg.width
    imgH = imageOrg.height

    pic = $('#popup #pic')
    pic.attr 'src', url

    if (winW > imgW && winH > imgH)
      pic.css 'width', imgW + 'px'
      pic.css 'height', imgH + 'px'
      pic.css 'left', ((winW - imgW)/2) + 'px'
      pic.css 'top', ((winH - imgH)/2) + 'px'
    else
      if (imgH * newW / imgW < imgH - 200)
        newW = winW - 200
        newH = imgH * newW / imgW
        pic.css 'width', newW + 'px'
        pic.css 'height', newH + 'px'
        pic.css 'top', ((winH - newH)/2) + 'px'
        pic.css 'left', ((winW - newW)/2) + 'px'
      else
        newH = winH - 200
        newW = imgW * newH / imgH
        pic.css 'width', newW + 'px'
        pic.css 'height', newH + 'px'
        pic.css 'left', ((winW - newW)/2) + 'px'
        pic.css 'top', ((winH - newH)/2) + 'px'

@hidePopup = () ->
  popup = $('#popup')
  popup.css 'display', 'none'
