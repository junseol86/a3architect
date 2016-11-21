$ ->
  image_process('.image_fit')

$(window).resize () ->
  image_process '.image_fit'

@image_process = (name) ->
  $(name).each () ->
    divW = $(this).width()
    divH = $(this).height()

    imgSrc = $(this).css('background-image').replace('url("', '').replace('")', '')
    imageOrg = new Image()
    imageOrg.src = imgSrc
    imageOrg.onload = () ->
      imgW = imageOrg.width
      imgH = imageOrg.height

      bgW = 0
      bgH = 0
      offX = 0
      offY = 0

      if (imgW/imgH > divW/divH)
        bgW = imgW * (divH / imgH)
        bgH = divH
        offX = -(bgW - divW)/2
      else
        bgW = divW
        bgH = imgH * (divW / imgW)
        offY = -(bgH - divH)/2

#      alert imgW + " " + imgH + " " + bgW + " " + bgH + " " + divW + " " + divH + " " + offX + " " + offY

      $(name).css 'background-size', bgW + 'px ' + bgH + 'px'
      $(name).css 'background-position', offX + 'px ' + offY + 'px'
