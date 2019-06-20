package com.example.marek.sledzeniepaczek.data

import com.example.marek.sledzeniepaczek.couriers.Couriers
import java.io.Serializable

class Packages(val courier: Couriers, val packageNumber: String) : Serializable