package org.analyzer;


public enum SequenceTypeDepractedTest {
    RNA(SequenceTypeDepractedTest.NucleotideWeight.A(329.2),
        SequenceTypeDepractedTest.NucleotideWeight.C(305.2),
        SequenceTypeDepractedTest.NucleotideWeight.G(345.2),
        SequenceTypeDepractedTest.NucleotideWeight.U(306.2)
    ),
    DNA(SequenceTypeDepractedTest.NucleotideWeight.A(313.21),
        SequenceTypeDepractedTest.NucleotideWeight.C(289.18),
        SequenceTypeDepractedTest.NucleotideWeight.G(329.21),
        SequenceTypeDepractedTest.NucleotideWeight.T(304.20)
    ),
    PEPTIDE(
            SequenceTypeDepractedTest.AminoAcidWeight.C(8.33),
            SequenceTypeDepractedTest.AminoAcidWeight.D(3.86),
            SequenceTypeDepractedTest.AminoAcidWeight.E(4.25),
            SequenceTypeDepractedTest.AminoAcidWeight.H(6.0),
            SequenceTypeDepractedTest.AminoAcidWeight.K(10.53),
            SequenceTypeDepractedTest.AminoAcidWeight.R(12.48),
            SequenceTypeDepractedTest.AminoAcidWeight.Y(10.07),
            SequenceTypeDepractedTest.AminoAcidWeight.N_TERM(9.69),
            SequenceTypeDepractedTest.AminoAcidWeight.C_TERM(2.34)
    ),
    AMBIGOUS(0);

    private Object[] molecularWeight;

    SequenceTypeDepractedTest(Object... molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public Object getMolecularWeight() {
        return molecularWeight;
    }

    private static class NucleotideWeight {
        private final double molecularWeight;

        private NucleotideWeight(double molecularWeight) {
            this.molecularWeight = molecularWeight;
        }

        public static NucleotideWeight A(double molecularWeight) {
            return new NucleotideWeight(molecularWeight);
        }

        public static NucleotideWeight T(double molecularWeight) {
            return new NucleotideWeight(molecularWeight);
        }

        public static NucleotideWeight G(double molecularWeight) {
            return new NucleotideWeight(molecularWeight);
        }

        public static NucleotideWeight C(double molecularWeight) {
            return new NucleotideWeight(molecularWeight);
        }

        public static NucleotideWeight U(double molecularWeight) {
            return new NucleotideWeight(molecularWeight);
        }
    }

    private static class AminoAcidWeight {
        private final double molecularWeight;

        private AminoAcidWeight(double molecularWeight) {
            this.molecularWeight = molecularWeight;
        }

        public static AminoAcidWeight C(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight D(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight E(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight H(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight K(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight R(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight Y(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight N_TERM(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

        public static AminoAcidWeight C_TERM(double molecularWeight) {
            return new AminoAcidWeight(molecularWeight);
        }

    }

}
