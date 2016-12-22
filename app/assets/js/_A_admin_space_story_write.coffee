@uploadedImageProcess = (result) ->
  url = result.replace('@', '')
  $('#ss_thumbnail').css 'background-image', "url('http://#{url}')"
  imageProcessor().image_process('#ss_thumbnail')
  $('#thumbnail_to_submit').val(url)
  boardModule().putPickThumbnailForm()
