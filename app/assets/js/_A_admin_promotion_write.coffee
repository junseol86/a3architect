@uploadedImageProcess = (result) ->
  url = result.replace('@', '')
  $('#prom_thumbnail').css 'background-image', "url('http://#{url}')"
  imageProcessor().image_process('#prom_thumbnail')
  $('#thumbnail_to_submit').val(url)
  boardModule().putPickThumbnailForm()
