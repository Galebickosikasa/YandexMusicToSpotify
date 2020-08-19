from yandex_music.client import Client

def our_func (user, password):
    client = Client.from_credentials (user, password)
    my_list = client.users_likes_tracks ().tracks

    a = ''

    for x in my_list: 
        track = client.tracks (x.id)[0]
        title = track.title
        name = ''
        for artist in track.artists:
            name += artist.name + ', '
        name = name[:-2]
        uri = track.cover_uri
        uri = uri[:-2]
        uri = 'http://' + uri + '400x400'
        # urllib.request.urlretrieve (uri, title + '.jpg')

        a += '{' + title + ' ~ ' + name + ' ~ ' + uri + '}'

    return a
