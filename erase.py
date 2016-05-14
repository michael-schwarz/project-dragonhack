import schedule
import time


def daily_erase():
	# sql query
	print ("Run daily erase")
	if time.strftime("%d") == "1":
		# sql query
		print ("Run monthly erase")
	

schedule.every().day.at("00:00").do(daily_erase)

while True:
    schedule.run_pending()
    time.sleep(1) # wait one second