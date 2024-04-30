

from PyQt5.QtWidgets import *
from PyQt5 import QtCore, QtGui
from PyQt5.QtGui import *
from PyQt5.QtCore import *
from personal_best import PersonalBest
import random
import sys


class BinSearch(QMainWindow):
	"""
	Klasa dotycząca całej gry w zgadywanie liczby
	
	Poziomy trudności:
	1: liczba z przedziału 1-10
	2: liczba z przedziału 1-100
	3: liczba z przedziału 1-1000
	"""
	def __init__(self,difficulty):
		"""
		Metoda inicjalizująca
		"""
		super().__init__()

		self.setWindowTitle("Project Game, Binsearch ")

		self.setGeometry(100, 100, 340, 350)

		self.difficulty = 10**difficulty
		self.score = 0

		self.personal_best = PersonalBest('b',difficulty) 

		self.UiComponents()

		self.show()

		

	def UiComponents(self):
		"""
		Ustawianie wszystkich elementów widocznych w grze
		"""
		head = QLabel("Number Guessing Game", self)

		head.setGeometry(20, 10, 300, 60)

		font = QFont('Times', 14)
		font.setBold(True)
		font.setItalic(True)
		font.setUnderline(True)

		head.setFont(font)

		head.setAlignment(Qt.AlignCenter)

		color = QGraphicsColorizeEffect(self)
		color.setColor(Qt.darkCyan)
		head.setGraphicsEffect(color)

		self.info = QLabel("Welcome", self)

		self.info.setGeometry(40, 85, 260, 80)

		self.info.setWordWrap(True)

		self.info.setFont(QFont('Times', 13))
		self.info.setAlignment(Qt.AlignCenter)

		self.info.setStyleSheet("QLabel"
								"{"
								"border : 2px solid black;"
								"background : lightgrey;"
								"}")

		self.spin = QSpinBox(self)
		
		self.spin.setRange(1, self.difficulty)

		self.spin.setGeometry(120, 170, 100, 60)

		self.spin.setAlignment(Qt.AlignCenter)
		self.spin.setFont(QFont('Times', 15))

		check = QPushButton("Check", self)

		check.setGeometry(130, 235, 80, 30)

		check.clicked.connect(self.check_action)

		start = QPushButton("Start", self)
		start.setGeometry(65, 280, 100, 40)

		reset_game = QPushButton("Reset", self)

		reset_game.setGeometry(175, 280, 100, 40)

		color_red = QGraphicsColorizeEffect()
		color_red.setColor(Qt.red)
		reset_game.setGraphicsEffect(color_red)

		color_green = QGraphicsColorizeEffect()
		color_green.setColor(Qt.darkBlue)
		start.setGraphicsEffect(color_green)

		start.clicked.connect(self.start_action)
		reset_game.clicked.connect(self.reset_action)

	def start_action(self):
		"""
		Metoda rozpoczynająca grę
		W grze na początku trzeba kliknąć "Start"
		"""
		self.info.setStyleSheet("QLabel"
								"{"
								"border : 2px solid black;"
								"background : lightgrey;"
								"}")
		self.number = random.randint(1, self.difficulty)
		self.info.setText(f"Try to guess number between 1 to {self.difficulty}")

	def check_action(self):
		"""
		Metoda, która rozpatruje wskazaną przez nas liczbę
		W przypadku braku trafienia dostajemy powiadomienie,
		czy wskazana przez nas liczba jest mniejsza lub większa od szukanej
		"""
		user_number =  self.spin.value()
		self.score += 1
		if user_number == self.number:
			self.personal_best.save(self.score)
			self.info.setText(f"Correct Guess, Your score is {self.score}, your personal best is {self.personal_best.read()} ")
			self.number = random.randint(1, self.difficulty)
			self.score = 0
			self.info.setStyleSheet("QLabel"
									"{"
									"border : 2px solid black;"
									"background : lightgreen;"
									"}")

		elif user_number < self.number:
			self.info.setText("Guessed number is smaller")
			
		else:
			self.info.setText("Guessed number is bigger")

	def reset_action(self):
		"""
		Metoda resetująca stan gry
		"""
		
		self.info.setStyleSheet("QLabel"
								"{"
								"border : 2px solid black;"
								"background : lightgrey;"
								"}")

		self.info.setText("Welcome")
		self.number = random.randint(1, self.difficulty)
		self.score = 0