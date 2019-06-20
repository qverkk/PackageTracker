from selenium import webdriver
from flask import Flask
import json
import re

class Package():
	def __init__(self, location, status, datetime):
		self.location = location
		self.status = status
		self.datetime = datetime

app = Flask(__name__)

@app.route("/dhl/<number>")
def dhl(number):
	driver = webdriver.PhantomJS('/phantomjs')
	driver.set_window_size(1120, 550)
	driver.implicitly_wait(15)
	driver.get("https://www.dhl.com.pl/en/express/tracking.html?AWB=%s" % number)
	elementList = []
	elements = driver.find_element_by_class_name('result-checkpoints').find_elements_by_tag_name('tbody')
	for x in elements:
		for y in x.find_elements_by_tag_name('tr'):
			tmp = y.find_elements_by_tag_name('td')
			location = tmp[2].get_attribute('innerHTML')
			status = tmp[1].get_attribute('innerHTML')
			time = tmp[3].get_attribute('innerHTML')
			obj = Package(location, status, time)
			res = obj.location + ',' + obj.status + ',' + obj.datetime + '<br/>'
			elementList.append(res)
	driver.quit()
	return ' '.join(elementList)

@app.route("/ups/<number>")
def ups(number):
	driver = webdriver.PhantomJS('/phantomjs')
	driver.set_window_size(1120, 550)
	driver.implicitly_wait(15)
	driver.get("https://www.ups.com/track?loc=pl_PL&tracknum=%s&requester=NES&agreeTerms=yes/trackdetails" % number)
	element = driver.find_element_by_tag_name('tbody')
	elements = element.find_elements_by_tag_name('tr')
	elementList = []
	for x in elements:
		tmp = x.find_elements_by_tag_name('td')
		location = tmp[2].find_element_by_tag_name('span').get_attribute('innerHTML')
		location = re.sub('[,]+', '', location)
		status = tmp[0].get_attribute('innerHTML')
		timeSpans = tmp[1].find_elements_by_tag_name('span')
		time = "%s %s" % (timeSpans[0].get_attribute('innerHTML'), timeSpans[1].get_attribute('innerHTML'))
		obj = Package(location, status, time)
		res = obj.location + ',' + obj.status + ',' + obj.datetime + '<br/>'
		elementList.append(res)
	driver.quit()
	return ' '.join(elementList)

@app.route("/pp/<number>")
def pp(number):
	driver = webdriver.PhantomJS('/phantomjs')
	driver.set_window_size(1120, 550)
	driver.get('https://emonitoring.poczta-polska.pl/?numer=%s' % number)
	element = driver.find_element_by_id('zadarzenia_td')
	elementList = []
	elements = element.find_elements_by_tag_name('tr')
	tmp = 0
	for x in elements:
		if tmp > 2:
			ids = x.find_elements_by_tag_name('td')
			status = ids[0].get_attribute('innerHTML')
			time = ids[1].get_attribute('innerHTML')
			location2 = ids[2]
			location = ""
			if location2.find_elements_by_tag_name('a'):
				location = location2.find_element_by_tag_name('a').get_attribute('innerHTML')
			else:
				location = location2.get_attribute('innerHTML')
			obj = Package(location, status, time)
			res = obj.location + ',' + obj.status + ',' + obj.datetime + '<br/>'
			elementList.append(res)
		tmp += 1
	return ' '.join(elementList)

@app.route("/fedex/<number>")
def fedex(number):
	driver = webdriver.PhantomJS('/phantomjs')
	driver.set_window_size(1120, 550)
	driver.implicitly_wait(15)
	driver.get("https://www.fedex.com/apps/fedextrack/?action=track&cntry_code=en&trackingnumber=%s" % number)
	element = driver.find_element_by_class_name('collapsableArea')
	elements = element.find_elements_by_tag_name('ul')
	elementList = []
	for x in elements:
		for y in x.find_elements_by_tag_name('li'):
			spans = y.find_elements_by_tag_name('span')
			time = y.find_element_by_tag_name('div').get_attribute('innerHTML') + ', ' + spans[0].get_attribute('innerHTML')
			data = spans[1].find_elements_by_tag_name('div')
			location = data[0].get_attribute('innerHTML')
			status = data[1].get_attribute('innerHTML')
			obj = Package(location, status, time)
			res = obj.location + ',' + obj.status + ',' + obj.datetime + '<br/>'
			elementList.append(res)
	driver.quit()
	return ' '.join(elementList)

@app.route("/inpost/<number>")
def inpost(number):
	driver = webdriver.PhantomJS('/phantomjs')
	driver.set_window_size(1120, 550)
	driver.implicitly_wait(60)
	driver.get('https://api-shipx-pl.easypack24.net/v1/tracking/%s' % number)
	element = driver.find_element_by_tag_name('pre')
	"""
	driver.get("https://inpost.pl/sledzenie-przesylek?number=%s" % number)
	element = driver.find_element_by_class_name('tracking__results-item--wrapper')
	print element.get_attribute('innerHTML')
	leftSide = []
	rightSide = []
	elements = element.find_elements_by_class_name('tracking_datetime')
	print len(elements)
	for x in elements:
		leftSide.append(x.get_attribute('innerHTML'))
	elements = element.find_elements_by_class_name('tracking_details')
	for x in elements:
		title = x.find_element_by_class_name('tracking_title').get_attribute('innerHTML')
		description = x.find_element_by_class_name('tracking_description').get_attribute('innerHTML')
		rightSide.append(title + "\n" + description)
	"""
	result = element.get_attribute('innerHTML')
	driver.quit()
	jsonElement = json.loads(result)
	statuses = []
	for x in jsonElement['tracking_details']:
		tmp = "NAN, %s, %s<br/>" % (x['status'], x['datetime'])
		statuses.append(tmp)
	return ' '.join(statuses)

app.run('0.0.0.0', port = 80)
