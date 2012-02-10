import time
import pylast

API_KEY = "1f581c9a53088523da61c549a57a2d6f"
API_SECRET = "92f88bf1b1b3dee1dc18caf34bab5be5"

ARTIST_A = "Symphony X"
ARTIST_B = "Death Angel"

TRACK_A = "For Whom the Bell Tolls"
TRACK_B = "Symphony of Destruction"

W_S = 0.5 # weight on similar artists
W_T = 0.5 # weight on similar tags
THRESHOLD = 0.2 # min score to be similar

SIMILAR_LIMIT = 20 # number of similar artists to examine
TAG_LIMIT = 5

LOC_1 = (39.95419, -75.19535) # Sansom East
LOC_2 = (39.95199, -75.20109) # Harrison

MAX_DISTANCE = 50 * 0.000621371192 # meters * miles/meter

## Handle for Last.FM API calls
network = pylast.LastFMNetwork(api_key = API_KEY, api_secret = API_SECRET)

## Comparison algorithm that returns whether artist1 and artist2 are similar
"""
def compare(track1, track2, loc1=None, loc2=None):

    ## Grab the top search result for the artists from Last.fm
    # search takes ~2-3 sec
    t1 = network.search_for_track("",track1).get_next_page()[0]
    t2 = network.search_for_track("",track2).get_next_page()[0]
    a1 = t1.get_artist()
    a2 = t2.get_artist()
    #a1 = network.get_artist(artist1)
    #a2 = network.get_artist(artist2)

    print a1, a2

    ## Grab similar artists
    similar = a1.get_similar(limit=SIMILAR_LIMIT)
    similar_artists_1 = map(lambda x: x[0], similar) # grab Artists
    #similar = a2.get_similar(limit=SIMILAR_LIMIT)
    #similar_artists_2 = map(lambda x: x[0], similar) # grab Artists

    ## Grab top tags
    tag_items = a1.get_top_tags(limit=TAG_LIMIT)
    tags_1 = set(map(lambda x: x[0].get_name(), tag_items))
    tag_items = a2.get_top_tags(limit=TAG_LIMIT)
    tags_2 = set(map(lambda x: x[0].get_name(), tag_items))

    #print tags_1, tags_2, tags_1 & tags_2

    #print map(lambda x: x.get_name(), similar_artists)

    s = a2 in similar_artists_1 #or a1 in similar_artists_2
    t = len(tags_1 & tags_2) / (1.0 * TAG_LIMIT)

    score =  W_S * s + W_T * t

    #print score
    return score >= THRESHOLD
"""
def compare(artist1, artist2, loc1=(0.0,0.0), loc2=(0.0,0.0)):

    if loc1 == None or loc1 == (None, None): loc1 = (0.0,0.0)
    if loc2 == None or loc2 == (None, None): loc2 = (0.0,0.0)

    ## Check distance using approximation
    x = 69.1 * (loc1[0] - loc2[0]) # lat
    y = 53.0 * (loc1[1] - loc2[1]) # lon
    dist = (x**2 + y**2) ** (1/2.)

    if dist > MAX_DISTANCE: return False

    ## Grab the top search result for the artists from Last.fm
    # search takes ~2-3 sec
    #a1 = network.search_for_artist(artist1).get_next_page()[0]
    #a2 = network.search_for_artist(artist2).get_next_page()[0]
    a1 = network.get_artist(artist1)
    a2 = network.get_artist(artist2)

    ## Grab similar artists
    similar = a1.get_similar(limit=SIMILAR_LIMIT)
    similar_artists_1 = map(lambda x: x[0], similar) # grab Artists
    #similar = a2.get_similar(limit=SIMILAR_LIMIT)
    #similar_artists_2 = map(lambda x: x[0], similar) # grab Artists

    ## Grab top tags
    tag_items = a1.get_top_tags(limit=TAG_LIMIT)
    tags_1 = set(map(lambda x: x[0].get_name(), tag_items))
    tag_items = a2.get_top_tags(limit=TAG_LIMIT)
    tags_2 = set(map(lambda x: x[0].get_name(), tag_items))

    #print tags_1, tags_2, tags_1 & tags_2

    #print map(lambda x: x.get_name(), similar_artists)

    s = a2 in similar_artists_1 #or a1 in similar_artists_2
    t = len(tags_1 & tags_2) / (1.0 * TAG_LIMIT)

    score =  W_S * s + W_T * t

    #print score
    return score >= THRESHOLD

#t1 = time.time()
#print ARTIST_A, ARTIST_B, compare(ARTIST_A, ARTIST_B, LOC_1, LOC_2)
#print compare(TRACK_A, TRACK_B)
#t2 = time.time()
#print "took %0.3f s" % (t2-t1)

if __name__ == "__main__":
    import doctest
    doctest.testfile("tests.txt")
