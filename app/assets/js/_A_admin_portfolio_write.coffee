@uploadedImageProcess = (result) ->
  if (result.indexOf('@') > -1)
    url = result.replace('@', '')
    $('#pf_thumbnail').css 'background-image', "url('http://#{url}')"
    imageProcessor().image_process('#pf_thumbnail')
    $('#thumbnail_to_submit').val(url)
    boardModule().putPickThumbnailForm()

  if (result.indexOf('$') > -1)
    url = result.replace('$', '')
    $('#pf_in_charge_photo').css 'background-image', "url('http://#{url}')"
    imageProcessor().image_process('#pf_in_charge_photo')
    $('#thumbnail_to_submit').val(url)
    boardModule().putPickInChargePhotoForm()
