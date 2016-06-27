package bam.core;

public interface BAMProperty<T> {
	T getValue();
	void setValue( T value );
}