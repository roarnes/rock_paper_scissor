import time, socket, sys
import string

print('Setup Server...')

time.sleep(1)
#Get the hostname, IP Address from socket and set Port
soc = socket.socket()
# host_name = socket.gethostname()
host_name = '192.168.43.19'
ip = socket.gethostbyname(host_name)
port = 7777

conn = 'CONNECTED'
clos = 'CLOSED'

#allowing 2 clients
tot_socket = 2

#binding
soc.bind((host_name, port))
print(host_name, '({})'.format(ip))

soc.listen(1) #Try to locate using socket
print('Waiting for incoming connections...')

connection1, addr1 = soc.accept()
print("Received connection from ", addr1[0], "(", addr1[1], ")\n")
print('Connection Established. Connected From: {}, ({})'.format(addr1[0], addr1[0]))
connection1.send(conn.encode())
player = '1';
connection1.send(player.encode())


print('Waiting for the second player...')

connection2, addr2 = soc.accept()
print("Received connection from ", addr2[0], "(", addr2[1], ")\n")
print('Connection Established. Connected From: {}, ({})'.format(addr2[0], addr2[0]))
connection2.send(conn.encode())
player = '2';
connection2.send(player.encode())

go = 'GO'
wait = 'WAIT'
while True:
   message1 = connection1.recv(1024)
   message1 = message1.decode('utf-8')

   connection1.send(wait.encode())
   connection2.send(go.encode())

   message2 = connection2.recv(1024)
   message2 = message2.decode('utf-8')

   print('Player1 >', message1)
   print('Player2 >', message2)

   r = '\x00\x03[R]'
   p = '\x00\x03[P]'
   s = '\x00\x03[S]'
   d = '\x00\x03[D]'

   if message1 == message2:
      if message1 == d or message2 == d:
         message = clos
         connection1.send(message.encode())
         connection2.send(message.encode())
         break

      else:
         m1 = 'DRAW'
         m2 = 'DRAW'
         connection1.send(m1.encode())
         connection2.send(m2.encode())

   elif message1 == r and message2 == p:
      m1 = 'LOSE'
      m2 = 'WIN'
      connection1.send(m1.encode())
      connection2.send(m2.encode())

   elif message1 == p and message2 == r:
      m1 = 'WIN'
      m2 = 'LOSE'
      connection1.send(m1.encode())
      connection2.send(m2.encode())

   elif message1 == r and message2 == s:
      m1 = 'WIN'
      m2 = 'LOSE'
      connection1.send(m1.encode())
      connection2.send(m2.encode())

   elif message1 == s and message2 == r:
      m1 = 'LOSE'
      m2 = 'WIN'
      connection1.send(m1.encode())
      connection2.send(m2.encode())

   elif message1 == p and message2 == s:
      m1 = 'LOSE'
      m2 = 'WIN'
      connection1.send(m1.encode())
      connection2.send(m2.encode())

   elif message1 == s and message2 == p:
      m1 = 'WIN'
      m2 = 'LOSE'
      connection1.send(m1.encode())
      connection2.send(m2.encode())

   connection1.send(go.encode())
   connection2.send(wait.encode())
   

