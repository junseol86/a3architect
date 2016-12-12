$ ->
  imageProcessor().image_process('.image_fit')
  imageProcessor().set_max_width()

$(window).resize () ->
  imageProcessor().image_process '.image_fit'

@imageProcessor = () -> {

#  주어진 클래스,아이디의 div 내 배경화면을 꽉 차게
  image_process: (name) ->
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
        $(name).css 'background-size', bgW + 'px ' + bgH + 'px'
        $(name).css 'background-position', offX + 'px ' + offY + 'px'

#  게시판 내 이미지들이 게시판 너비를 넘지 않도록
  set_max_width: () ->
    maxWidth = $('#width_checker').width()

    $('#board_content_container img').each () ->
      imgWidth = $(this).width()
      imgHeight = $(this).height()

      if(imgWidth > maxWidth)
        $(this).css "width", "#{maxWidth}.px"
        $(this).css "width", "#{imgHeight * maxWidth / imgWidth}.px"
}
