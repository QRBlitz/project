import numpy as np
from qiskit import QuantumCircuit, execute, quantum_info as qi
from qiskit_aer import Aer

def bb84_key_exchange():
    num_qubits = 30

    qc = QuantumCircuit(num_qubits, num_qubits)

    alice_bases = np.random.choice(['+', 'x'], size=num_qubits)
    alice_bits = np.random.randint(2, size=num_qubits)

    for i in range(num_qubits):
        if alice_bits[i] == 1:
            qc.x(i)
        if alice_bases[i] == 'x':
            qc.h(i)
    qc.barrier()

    bob_bases = np.random.choice(['+', 'x'], size=num_qubits)
    for i in range(num_qubits):
        if bob_bases[i] == 'x':
            qc.h(i)
    qc.measure(range(num_qubits), range(num_qubits))

    backend = Aer.get_backend('qasm_simulator')
    result = execute(qc, backend, shots=1).result()
    measurements = result.get_counts()

    bob_measurements = list(measurements.keys())[0]

    key_bits = []
    for i in range(num_qubits):
        if alice_bases[i] == bob_bases[i]:
            key_bits.append(int(bob_measurements[num_qubits - i - 1]))
    key_bits = key_bits[:128]

    key_bytes = int("".join(str(x) for x in key_bits), 2).to_bytes(16, byteorder='big')
    print(key_bytes)



bb84_key_exchange()