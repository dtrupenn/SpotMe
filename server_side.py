# connects to MySQL database
# creates a connection to the app
# gets data from the connection
# stores data into database
# queries database and runs comparison algorithm
# returns result of comparison

import ast
import socket
import fcntl
import struct
import MySQLdb
import json
from algo import compare

# socket configuration
__PORT = 80
__RECV_BUF = 4096
__MAX_CONNECT = 5

# get IP address, workaround to grab correct IP
# because gethostname() and gethostbyname() was returning localhost
# 0x8915 is SIOCGIFADDR
def get_ip_address(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return socket.inet_ntoa(fcntl.ioctl(s.fileno(), 0x8915,
                                struct.pack('256s', ifname[:15]))[20:24])

# connect to database
sqldb = MySQLdb.connect(host = "localhost", user = "root", passwd = "headsh0t", db = "pennapps_s2012_test")
writer = sqldb.cursor()
query = sqldb.cursor()

# create socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((get_ip_address('wlan0'), __PORT))

s.listen(__MAX_CONNECT)

# accept connections, grab data, analyze data
while 1:
    # connect to a client
    (client, address) = s.accept()
    
    # grabs data, splits on newline, strips "json: " and whitespace, and converts into a dict
    data = ast.literal_eval(str.strip(str.split(client.recv(__RECV_BUF), '\n')[1][6:]))
    # print data

    # check if it's song info or location info
    if 'latitude' in data:
        query.execute("""
                      SELECT fid FROM location_info
                      WHERE fid = %s
                      """, (data['FID']))
        result = query.fetchall()
        # print "1"

        if result: # user exists
            writer.execute("""
                           UPDATE location_info
                           SET latitude = %s,
                               longitude = %s
                           WHERE
                               fid = %s
                          """, (data['lat'], data['long'], data['FID']))
            # print "2"
        else: # user doesn't exist
            writer.execute("""
                           INSERT INTO location_info (fid, latitude, longitude)
                           VALUES
                           (%s, %s, %s)
                          """, (data['FID'] , data['lat'], data['long']))
            # print "3"
    else:
        query.execute("""
                      SELECT fid FROM song_info2
                      WHERE fid = %s
                      """, (data['FID']))
        result = query.fetchall()
        # print "4"

        if result:
            writer.execute("""
                           UPDATE song_info2
                           SET album = %s,
                               title = %s,
                               name = %s,
                               artist = %s
                           WHERE
                               fid = %s
                          """, (data['Album'], data['Title'], data['Name'], data['Artist'], data['FID']))
            # print "5"
        else:
            writer.execute("""
                           INSERT INTO song_info2 (fid, album, title, name, artist)
                           VALUES
                           (%s, %s, %s, %s, %s)
                          """, (data['FID'] , data['Album'], data['Title'], data['Name'], data['Artist']))
            # print "6"
    
    # grab all users currently in database that is not the connected user
    query.execute("""
                  SELECT artist, latitude, longitude, album, title, name FROM joined_table
                  WHERE fid != %s
                  """, (data['FID']))
    result = query.fetchall()
    
    # print result
    # print "r"
    # for item in result:
        # print item
        # print "i"

    # print "7"
    
    # grab current user's data
    query.execute("""
                  SELECT artist, latitude, longitude FROM joined_table
                  WHERE fid = %s
                  """, (data['FID']))
    current_user = query.fetchall()

    # print current_user
    # print "c"
    # for item in current_user:
        # print item
        # print "i"

    # print "8"

    # run compare on all pairs of (current user artist, other user artist, current location, other location)
    r = []
    if result and current_user:
        # print "9"
        for item in result:
            # print current_user[0][0], item[0], current_user[0][1], current_user[0][2], item[1], item[2]
            # print "b"
            b = compare(current_user[0][0], item[0], (current_user[0][1], current_user[0][2]), (item[1], item[2]))
            if b:
                # print item[0], item[1], item[2], item[3], item[4], item[5]
                # print "a"
                r.append({"Artist": item[0], "Latitude": item[1], "Longitude": item[2], "Album": item[3], "Title": item[4], "Name": item[5]})
                # print r
                # print "10"
   
    # print r
    # ba = bytearray(str(json.dumps({"key": "test"})), "utf-8")
    ba = bytearray(str(json.dumps({"k": r})))
    
    # send results of compare back to current user and close connection
    response_str = "HTTP/1.1 200 OK\r\nContent-Length: %d\r\nConnection: close\r\n\r\n%s" %(len(ba), ba)
    client.send(response_str)
    client.close()
    # print "11"

# close socket and db
query.close()
writer.close()
s.close()

