$ ->
  image_process()

$(window).resize () ->
  image_process()

@image_process = () ->
  $('.image_fit').each () ->
    divW = $(this).width()
    divH = $(this).height()

    imgSrc = $(this).css('background-image').replace('url("', '').replace('")', '')
    imageOrg = new Image()
    imageOrg.src = imgSrc
    imgW = imageOrg.width
    imgH = imageOrg.height

    bgW = 0
    bgH = 0
    offX = 0
    offY = 0

    if (imgW/imgH > divW/divH)
      bgW = imgW * (divH / imgH)
      bgH = divH
      offX = (divW - imgW)/2
    else
      bgW = divW
      bgH = imgH * (divW / imgW)
      offY = (divH - imgH)/2

    $(this).css 'background-size', bgW + 'px ' + bgH + 'px'
    $(this).css 'background-position', offX + 'px ' + offY + 'px'
