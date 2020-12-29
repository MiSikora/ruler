# Changelog
All notable changes to this project will be documented in this document.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- `Distance.Epsilon` constant that represents smallest possible change in of `Distance`.
- `LengthUnits.bounds` property to the public API. It holds min and max distances that only that unit can represent as a natural count of self.
- `Length.withFittingUnit(units, fitter)` that selects the best unit for a length using supplied units and fitting algorithm. A complementary `UnitFitter` interface is added along with `InRangeUnitFitter` and `LogDistanceUnitFitter` implementations.

### Changed
- Added one nanometer to `Distance.Min`. This change was made in order to make `Distance.abs()` operation safe.

### Deprecated
- `Length.withAutoUnit()` in favour of `Length.withFittingUnit()` with default arguments.

## [1.0.0] - 2020-12-28

### Added
- `AutoLengthFormatter` class. Use it to format lengths using resources defined in the library based on a unit.
- `FlooredLengthFormatter` class. Use it to floor a length unit quantities and format them using resources defined in the library.
- `AutoFitLengthConverter` class. Use it to convert lengths to different lengths a best fitting units based on `Length.withAutoUnit()` and unit coercion.
- `LengthFormatter.Factory` interface. Use it to install custom formatters in `Ruler`.
- `LengthConverter.Factory` interface. Use it to install custom converters in `Ruler`.
- `Distance.abs()` and `Length.abs()` methods.
- `Ruler` implements now `LengthConverter` and `LengthFormatter`.

### Changed
- `Distance` and `Length` throw now `ArithmeticException` in case of failures due to overflows or math operations.
- `Distance.exactTotalMeters` property name to `meters`.
- `LengthUnit` is no longer an interface. It is now a sealed class with two implementations – `SiLengthUnit` and `ImperialLengthUnit`.
- `LengthUnit` is no longer `Iterable`. It exposes now `units` property.
- Custom `LengthFormatter`s must be now installed using `LengthFormatter.Factory` interface and `Ruler.addFormatterFactory()` method.
- `LengthFormatter` to a functional interface.
- `LengthFormatter` can no longer return `null`.
- `Length` no longer changes units automatically based on `Locale` during formatting.
- Order of arguments in `LengthFormatter.format()` method.
- `ImperialLengthFormatter` substitutes `ImperialDistanceFormatter`. It offers a more unified API without bloated configurability.
- Custom `LengthConverter`s must be now installed using `LengthConverter.Factory` interface and `Ruler.addConverterFactory()` method.
- `LengthConverter` to a functional interface.
- `LengthConverter` can no longer return `null`.
- Upgrade to Kotlin `1.4.21`.

### Removed
- Explicit Java support with `@Jvm*` annotations.
- `Distance.metersPart` and `Distance.nanosPart` properties.
- `Distance.create(meters, nanometers)` method.
- `Distance.format()` and `Distance.formatFloored()` overloads that accept `LengthUnit`.
- `Ruler.flooredFormatters` property. Flooring is now available solely via `FlooredLengthFormatter`.

### Fixed
- Wrong lower meter bound of `Miles` unit.
- Wrong name of an argument in `Length.div()` methods.
- Imperial formatting not handling negative distances.

## [0.6.0] - 2020-11-11

### Added
- `meterRatio` property to `LengthUnit` interface.
- `contains` function to `LengthUnit` interface. This allows to use `in` sugar syntax.

### Changed
- `io.mehow.ruler:android` to `io.mehow.ruler:ruler-android` artifact.
- Changelog format follows now [Keep a Changelog](https://keepachangelog.com/) format. Format is applied retroactively to this file.
- `Length.measureLength` property to `measure`.
- `Distance.min`, `Distance.zero` and `Distance.max` respectively to `Distance.Min`, `Distance.Zero` and `Distance.Max`. They are now available as fields from Java.
- `ImperialDistanceFormatter.basic` and `ImperialDistanceFormatter.full` to `ImperialDistanceFormatter.Basic` and `ImperialDistanceFormatter.Full` respectively.
- Upgrade to Kotlin `1.4.10`.

### Removed
- `toDistance()` methods from `LengthUnit` interface.
- `-Xjvm-default=enable` compiler argument is no longer required.
- `toMeasuredLength()` function from `LengthUnit` interface.
- `appliesRangeTo()` function from `LengthUnit` interface.

## [0.5.3] - 2020-06-14

### Changed
- Upgrade to Kotlin `1.3.72`.

### Fixed
- Long to double conversion when computing distances.

## [0.5.2] - 2020-04-15

### Added
- Arabic language support.

### Fixed
- Fix a bug with no inches displayed for 0 length.

## [0.5.1] - 2020-03-01

### Removed
- Remove `totalMeters` property from `Distance` that approximates it.

### Fixed
- Fix wrong unit displayed for feet.

## [0.5.0] - 2020-02-29

### Added
- Support for floored formatting of any length units.
- Allow to set globally if UK should use imperial or SI units.

## [0.4.1] - 2020-02-07

### Added
- Support for negative distances.
- Support for floored SI unit lengths formatting. It displays only whole parts of a unit.

## [0.4.0] - 2020-02-05

### Added
- `Ruler` class as a central point for distance and length formatting.
- Imperial unit formatter. It allows to display measurements in a imperial-friendly format. For example `105ft 12in`.

## [0.3.2] 2020-02-03

### Fixed
- Fix issue with high precision Float and Double multiplication.

## [0.3.1] - 2020-02-03

### Fixed
- Make library JDK 7 compatible.

## [0.3.0] - 2020-02-03

### Added
- Add `Distance` (old `Length`) formatter for Android.
- Add basic math operators like multiplication and division.
- Add Double factories to `Distance` (old `Length`).

### Changed
- Swap names of `Distance` and `Length` classes.
- Use Long as an underlying `Distance` primitive. It is more suitable for application that have UI interaction and distance of ~975 light years seems sufficient for most day-to-day applications.

## [0.2.0] - 2020-02-03

### Changed
- `DistanceUnitCoercer` changed to `DistanceConverter`.
- `Distance` constructor is no longer public.
- `length` in `Distance` is no longer public.
- `meters` and `nanometers` properties renamed respectively to `metersPart` and `nanometersParts` in `Length` class.

## [0.1.0] - 2020-02-02

- Initial release.

[Unreleased]: https://github.com/MiSikora/ruler/compare/1.0.0...HEAD
[1.0.0]: https://github.com/MiSikora/ruler/releases/tag/1.0.0
[0.6.0]: https://github.com/MiSikora/ruler/releases/tag/0.6.0
[0.5.3]: https://github.com/MiSikora/ruler/releases/tag/0.5.3
[0.5.2]: https://github.com/MiSikora/ruler/releases/tag/0.5.2
[0.5.1]: https://github.com/MiSikora/ruler/releases/tag/0.5.1
[0.5.0]: https://github.com/MiSikora/ruler/releases/tag/0.5.0
[0.4.1]: https://github.com/MiSikora/ruler/releases/tag/0.4.1
[0.4.0]: https://github.com/MiSikora/ruler/releases/tag/0.4.0
[0.3.2]: https://github.com/MiSikora/ruler/releases/tag/0.3.2
[0.3.1]: https://github.com/MiSikora/ruler/releases/tag/0.3.1
[0.3.0]: https://github.com/MiSikora/ruler/releases/tag/0.3.0
[0.2.0]: https://github.com/MiSikora/ruler/releases/tag/0.2.0
[0.1.0]: https://github.com/MiSikora/ruler/releases/tag/0.1.0
