import numpy as np

from PyQt5.QtWidgets import *
from PyQt5 import QtCore, QtGui
from PyQt5.QtGui import *
from PyQt5.QtCore import *
from personal_best import PersonalBest
import random
import sys

def new_matrix(difficulty):
    """
    W funkcji tworzymy macierz w zależności od poziomu trudości
    oraz liczymy jej wyznacznik (zaokrąglamy go do najbliższej liczby całkowitej)
    """
    if difficulty == 1:
        matrix =  np.random.randint(-10,10,size=(2,2))
    elif difficulty == 2:
        matrix = np.random.randint(-10,10,size=(3,3))
    elif difficulty == 3:
        matrix = np.random.randint(-10,10,size=(4,4))

    determitant = round(np.linalg.det(matrix))
    return matrix, determitant

class TableModel(QAbstractTableModel):
	"""
	Klasa, dzięki której uzyskujemy naszą macierz w QT
	"""
	def __init__(self, data):
		super(TableModel, self).__init__()
		self._data = data

	def data(self, index, role):
		if role == Qt.DisplayRole:
			value = self._data[index.row(),index.column()]
			return str(value)

	def rowCount(self, index):
		return len(self._data)

	def columnCount(self, index):
		return len(self._data[0])



class Matrix(QMainWindow):
	"""
	Klasa dotycząca całej gry w liczenie wyznacznika
	
	Poziomy trudności:
	1: wyznacznik macierzy 2x2
	2: wyznacznik macierzy 3x3
	3: wyznacznik macierzy 4x4
	"""
	def __init__(self,difficulty):
		super().__init__()

		self.setWindowTitle("Project Game, Determitant ")

		self.setGeometry(100, 100, 700, 700)

		self.difficulty = difficulty
		self.new_matrix = new_matrix(difficulty)
		self.determitant = self.new_matrix[1]
		self.new_matrix = self.new_matrix[0]
		self.score = 0
		self.personal_best = PersonalBest('d',difficulty) 
		self.UiComponents()
		self.show()

		

	def UiComponents(self):
		"""
		Ustawianie wszystkich elementów widocznych w grze
		"""
		self.table = QTableView(self)
		self.table.setGeometry(20, 400, 550, 200)
		model = TableModel(self.new_matrix)
		self.table.setModel(model)


		head = QLabel("Matrix Game", self)
		head.setGeometry(20, 20, 300, 60)		

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
		
		self.spin.setRange(-100000, 100000)

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
		self.info.setText("Count the determitant of a matrix")

	def check_action(self):
		"""
		Metoda, która rozpatruje wskazaną przez nas liczbę
		jeżeli jest równa wyznacznikowi, to wygraliśmy
		"""
		user_number =  self.spin.value()
		self.score += 1
		if user_number == self.determitant:
			self.personal_best.save(self.score)
			self.info.setText(f"Correct Guess, Your score is {self.score}, your personal best is {self.personal_best.read()} ")
			self.score = 0
			self.info.setStyleSheet("QLabel"
									"{"
									"border : 2px solid black;"
									"background : lightgreen;"
									"}")
			
		else:
			self.info.setText("Wrong Number")

	def reset_action(self):
		"""
		Metoda resetująca stan gry
		Tworzymy przy tym nową macierz
		"""
		self.info.setStyleSheet("QLabel"
								"{"
								"border : 2px solid black;"
								"background : lightgrey;"
								"}")

		self.info.setText("Welcome")
		self.new_matrix = new_matrix(self.difficulty)
		self.determitant = self.new_matrix[1]
		self.new_matrix = self.new_matrix[0]
		model = TableModel(self.new_matrix)
		self.table.setModel(model)
		self.score = 0
